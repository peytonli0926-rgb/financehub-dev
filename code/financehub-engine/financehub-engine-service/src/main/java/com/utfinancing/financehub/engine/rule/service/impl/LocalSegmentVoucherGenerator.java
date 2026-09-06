package com.utfinancing.financehub.engine.rule.service.impl;

import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.redis.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class LocalSegmentVoucherGenerator {

    @Resource
    private RedisService redisService;

    // 本地号段缓存
    private final ConcurrentHashMap<String, Segment> segmentCache = new ConcurrentHashMap<>();
    private final ReentrantLock segmentLock = new ReentrantLock();

    // 配置参数
    private static final int SEGMENT_SIZE = 1000;
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 30 * 24 * 60 * 60L;

    @Data
    @AllArgsConstructor
    private static class Segment {
        private long current;
        private long end;
        private final Object lock = new Object();

        public boolean hasNext() {
            return current <= end;
        }

        public long getAndIncrement() {
            synchronized (lock) {
                if (current > end) {
                    return -1;
                }
                return current++;
            }
        }
    }

    /**
     * 使用本地号段预分配生成凭证号
     */
    public long generateVoucherNum(String voucherType, LocalDateTime dateTime) {
        String key = buildRedisKey(voucherType, dateTime);

        while (true) {
            Segment segment = segmentCache.get(key);

            if (segment == null || !segment.hasNext()) {
                segment = acquireNewSegment(key);
                if (segment == null) {
                    // 降级到原有Redis单号生成
                    log.error("LocalSegmentVoucherGenerator-本地号段申请失败，降级到Redis单号生成, key: {}", key);
                    return fallbackToRedis(key);
                }
                segmentCache.put(key, segment);
            }

            long voucherNum = segment.getAndIncrement();
            if (voucherNum != -1) {
                return voucherNum;
            }

            segmentCache.remove(key);
        }
    }

    private Segment acquireNewSegment(String key) {
        segmentLock.lock();
        try {
            // 双重检查
            Segment existing = segmentCache.get(key);
            if (existing != null && existing.hasNext()) {
                return existing;
            }

            // 使用多方案尝试生成号段
            long startNumber = generateSegmentWithMultipleStrategies(key, SEGMENT_SIZE);
            if (startNumber == -1) {
                return null;
            }

            log.info("LocalSegmentVoucherGenerator-成功申请号段, key: {}, 起始编号: {}, 结束编号: {}",
                    key, startNumber, startNumber + SEGMENT_SIZE - 1);
            return new Segment(startNumber, startNumber + SEGMENT_SIZE - 1);

        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-申请凭证号段异常, key: {}", key, e);
            return null;
        } finally {
            segmentLock.unlock();
        }
    }

    /**
     * 多策略生成号段
     */
    private long generateSegmentWithMultipleStrategies(String key, int segmentSize) {
        // 方案1：使用修复版INCRBY方案
        Long result = tryFixedIncrByStrategy(key, segmentSize);
        if (result != null) {
            return result;
        }

        // 方案2：使用纯GET/SET方案
        log.info("LocalSegmentVoucherGenerator-方案1失败，尝试纯GET/SET方案, key: {}", key);
        result = tryGetSetStrategy(key, segmentSize);
        if (result != null) {
            return result;
        }

        // 方案3：使用最简单的方案
        log.info("LocalSegmentVoucherGenerator-方案2失败，尝试最简单方案, key: {}", key);
        result = trySimpleStrategy(key, segmentSize);
        if (result != null) {
            return result;
        }
        log.error("LocalSegmentVoucherGenerator-所有方案都失败, key: {}", key);
        return -1;
    }

    /**
     * 方案1：修复版INCRBY方案
     */
    private Long tryFixedIncrByStrategy(String key, int segmentSize) {
        // 修复版Lua脚本 - 确保所有参数都是正确的类型
        String luaScript =
                "local key = KEYS[1]\n" +
                        "local increment_str = ARGV[1]\n" +
                        "local expire_str = ARGV[2]\n" +
                        "\n" +
                        "-- 转换为数字，确保类型正确\n" +
                        "local increment = tonumber(increment_str)\n" +
                        "local expire = tonumber(expire_str)\n" +
                        "\n" +
                        "-- 验证转换结果\n" +
                        "if increment == nil then\n" +
                        "    increment = 1000\n" +
                        "end\n" +
                        "if expire == nil then\n" +
                        "    expire = 2592000\n" +
                        "end\n" +
                        "\n" +
                        "-- 使用INCRBY原子操作\n" +
                        "local newValue = redis.call('INCRBY', key, increment)\n" +
                        "\n" +
                        "-- 如果是第一次设置（newValue等于increment），设置过期时间\n" +
                        "if newValue == increment then\n" +
                        "    redis.call('EXPIRE', key, expire)\n" +
                        "end\n" +
                        "\n" +
                        "-- 返回起始编号：newValue - increment + 1\n" +
                        "return newValue - increment + 1";

        List<String> keys = Collections.singletonList(key);
        List<String> args = Arrays.asList(
                String.valueOf(segmentSize),
                String.valueOf(REDIS_VOUCHER_NUM_EXPIRE));
        try {
            log.info("LocalSegmentVoucherGenerator-修复版INCRBY方案参数,脚本：{}, keys: {}, args: {}",luaScript, keys, args);
            Long result = redisService.executeLuaScript(luaScript, keys, args, Long.class);
            if (result != null) {
                log.info("LocalSegmentVoucherGenerator-修复版INCRBY方案成功, key: {}, 结果: {}", key, result);
                return result;
            }
        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-修复版INCRBY方案失败, key: {}, 错误: {}", key, e.getMessage());
        }
        return null;
    }

    /**
     * 方案2：纯GET/SET方案
     */
    private Long tryGetSetStrategy(String key, int segmentSize) {
        // 纯GET/SET方案，避免INCRBY
        String luaScript =
                "local key = KEYS[1]\n" +
                        "local increment_str = ARGV[1]\n" +
                        "local expire_str = ARGV[2]\n" +
                        "\n" +
                        "-- 转换为数字\n" +
                        "local increment = tonumber(increment_str)\n" +
                        "local expire = tonumber(expire_str)\n" +
                        "\n" +
                        "-- 设置默认值\n" +
                        "if increment == nil then increment = 1000 end\n" +
                        "if expire == nil then expire = 2592000 end\n" +
                        "\n" +
                        "-- 获取当前值\n" +
                        "local current = redis.call('GET', key)\n" +
                        "local startNumber\n" +
                        "\n" +
                        "if current == false or current == nil then\n" +
                        "    -- Key不存在，设置初始值\n" +
                        "    redis.call('SETEX', key, expire, tostring(increment))\n" +
                        "    startNumber = 1\n" +
                        "else\n" +
                        "    -- Key存在，转换为数字\n" +
                        "    current = tonumber(current)\n" +
                        "    if current == nil then\n" +
                        "        -- 如果当前值不是数字，删除并重新设置\n" +
                        "        redis.call('DEL', key)\n" +
                        "        redis.call('SETEX', key, expire, tostring(increment))\n" +
                        "        startNumber = 1\n" +
                        "    else\n" +
                        "        -- 正常情况：增加并返回\n" +
                        "        local newValue = current + increment\n" +
                        "        redis.call('SETEX', key, expire, tostring(newValue))\n" +
                        "        startNumber = current + 1\n" +
                        "    end\n" +
                        "end\n" +
                        "\n" +
                        "return startNumber";

        List<String> keys = Collections.singletonList(key);
        List<String> args = Arrays.asList(
                String.valueOf(segmentSize),
                String.valueOf(REDIS_VOUCHER_NUM_EXPIRE)
        );

        try {
            Long result = redisService.executeLuaScript(luaScript, keys, args, Long.class);
            if (result != null) {
                log.info("LocalSegmentVoucherGenerator-纯GET/SET方案成功, key: {}, 结果: {}", key, result);
                return result;
            }
        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-纯GET/SET方案失败, key: {}, 错误: {}", key, e.getMessage());
        }
        return null;
    }

    /**
     * 方案3：最简单方案 - 直接调用RedisService
     */
    private Long trySimpleStrategy(String key, int segmentSize) {
        try {
            log.info("LocalSegmentVoucherGenerator-使用最简单方案, key: {}", key);

            // 直接使用RedisService的方法，避免Lua脚本
            Boolean exists = redisService.hasKey(key);
            Long currentValue = null;

            if (exists != null && exists) {
                // Key存在，获取当前值
                currentValue = redisService.getCacheObject(key);
                log.info("LocalSegmentVoucherGenerator-Key存在, key: {}, 当前值: {}", key, currentValue);
            }

            long newValue;
            long startNumber;

            if (currentValue == null) {
                // Key不存在，设置初始值
                redisService.setCacheObject(key, segmentSize, REDIS_VOUCHER_NUM_EXPIRE, TimeUnit.SECONDS);
                log.info("LocalSegmentVoucherGenerator-设置初始值成功, key: {}, 起始编号: {}", key, segmentSize);
            } else {
                // Key存在，增加并设置新值
                newValue = currentValue + segmentSize;
                redisService.setCacheObject(key, newValue, REDIS_VOUCHER_NUM_EXPIRE, TimeUnit.SECONDS);
                log.info("LocalSegmentVoucherGenerator-更新值成功, key: {}, 新值: {}", key, newValue);
            }
            return currentValue;
        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-最简单方案失败, key: {}, 错误: {}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 降级到原有RedisService的单号生成方式
     */
    private long fallbackToRedis(String key) {
        try {
            long result = redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
            log.info("LocalSegmentVoucherGenerator-降级到单号生成成功, key: {}, 结果: {}", key, result);
            return result;
        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-Redis单号生成也失败，使用最终降级方案, key: {}", key, e);
            return generateFallbackNumber();
        }
    }

    /**
     * 最终降级方案
     */
    private long generateFallbackNumber() {
        try {
            // 使用时间戳+随机数生成相对唯一的数字
            long timestamp = System.currentTimeMillis();
            long random = ThreadLocalRandom.current().nextInt(10000);

            // 生成1-999999范围内的数字
            long number = (timestamp % 1000000) * 10000 + random;

            // 确保数字在合理范围内
            if (number < 1) {
                number = 1;
            } else if (number > 999999999) {
                number = number % 1000000000;
            }

            log.info("LocalSegmentVoucherGenerator-使用最终降级方案生成编号: {}", number);
            return number;
        } catch (Exception e) {
            log.error("LocalSegmentVoucherGenerator-最终降级方案也失败，返回固定值1", e);
            return 1;
        }
    }

    private String buildRedisKey(String voucherType, LocalDateTime dateTime) {
        return CacheConstants.COMMON_PREFIX + "_voucher_num_" +
                dateTime.getYear() + dateTime.getMonthValue() + "_" + voucherType;
    }

    /**
     * 获取当前本地号段状态（用于监控）
     */
    public Map<String, Object> getSegmentStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalSegments", segmentCache.size());

        Map<String, String> segmentDetails = new HashMap<>();
        segmentCache.forEach((key, segment) -> {
            long remaining = Math.max(0, segment.getEnd() - segment.getCurrent() + 1);
            segmentDetails.put(key,
                    String.format("current: %d, end: %d, remaining: %d, used: %d",
                            segment.getCurrent(), segment.getEnd(), remaining,
                            segment.getEnd() - segment.getCurrent() + 1 - remaining));
        });

        status.put("segments", segmentDetails);
        return status;
    }

    /**
     * 手动清理指定key的本地号段
     */
    public boolean clearSegment(String voucherType, LocalDateTime dateTime) {
        String key = buildRedisKey(voucherType, dateTime);
        Segment removed = segmentCache.remove(key);
        boolean success = removed != null;

        if (success) {
            log.info("LocalSegmentVoucherGenerator-成功清理本地号段, key: {}", key);
        } else {
            log.info("LocalSegmentVoucherGenerator-本地号段不存在, key: {}", key);
        }

        return success;
    }

    
    /**
     * 诊断指定key的状态
     */
    public Map<String, Object> diagnoseKey(String voucherType, LocalDateTime dateTime) {
        String key = buildRedisKey(voucherType, dateTime);
        Map<String, Object> diagnosis = new HashMap<>();

        try {
            diagnosis.put("key", key);

            // 检查key是否存在
            Boolean exists = redisService.hasKey(key);
            diagnosis.put("exists", exists);

            if (exists != null && exists) {
                // 获取key的当前值
                Object value = redisService.getCacheObject(key);
                diagnosis.put("value", value);
                diagnosis.put("valueType", value != null ? value.getClass().getSimpleName() : "null");

                // 检查值是否可以转换为数字
                if (value != null) {
                    try {
                        Long numericValue = Long.parseLong(value.toString());
                        diagnosis.put("isNumeric", true);
                        diagnosis.put("numericValue", numericValue);
                    } catch (NumberFormatException e) {
                        diagnosis.put("isNumeric", false);
                        diagnosis.put("parseError", e.getMessage());
                    }
                }

                // 获取过期时间
                Long expire = redisService.getExpire(key);
                diagnosis.put("expire", expire);
            }

            // 检查本地缓存
            Segment segment = segmentCache.get(key);
            if (segment != null) {
                Map<String, Object> segmentInfo = new HashMap<>();
                segmentInfo.put("current", segment.getCurrent());
                segmentInfo.put("end", segment.getEnd());
                segmentInfo.put("remaining", segment.getEnd() - segment.getCurrent() + 1);
                diagnosis.put("localSegment", segmentInfo);
            }

            diagnosis.put("success", true);
        } catch (Exception e) {
            diagnosis.put("success", false);
            diagnosis.put("error", e.getMessage());
        }

        return diagnosis;
    }

    /**
     * 获取当前活跃的号段数量
     */
    public int getActiveSegmentCount() {
        return segmentCache.size();
    }
}