package com.utfinancing.financehub.common.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
@Slf4j
public class RedisService {
    @Autowired
    public RedisTemplate redisTemplate;

    private static final long BEGIN_TIMESTAMP = 1640995200L;
    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    @Value("${redis.prefix:finhub}")
    private String redisPrefix;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        try {
            redisTemplate.opsForValue().set(redisPrefix + key, value);
        } catch (Exception e) {
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(redisPrefix + key, value, timeout, timeUnit);
        } catch (Exception e) {
        }
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        try {
            return expire(redisPrefix + key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        try {
            return redisTemplate.expire(redisPrefix + key, timeout, unit);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        try {
            return redisTemplate.getExpire(redisPrefix + key);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(redisPrefix+key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        try {
            ValueOperations<String, T> operation = redisTemplate.opsForValue();
            return operation.get(redisPrefix + key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        try {
            return redisTemplate.delete(redisPrefix + key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection) {
        try {
            return redisTemplate.delete(collection) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(redisPrefix+key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        try {
            return redisTemplate.opsForList().range(redisPrefix + key, 0, -1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(redisPrefix+key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        try {
            return redisTemplate.opsForSet().members(redisPrefix + key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        try {
            if (dataMap != null) {
                redisTemplate.opsForHash().putAll(redisPrefix + key, dataMap);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        try {
            return redisTemplate.opsForHash().entries(redisPrefix + key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        try {
            redisTemplate.opsForHash().put(redisPrefix + key, hKey, value);
        } catch (Exception e) {
        }
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        try {
            HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
            return opsForHash.get(redisPrefix + key, hKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        try {
            return redisTemplate.opsForHash().multiGet(redisPrefix + key, hKeys);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        try {
            return redisTemplate.opsForHash().delete(redisPrefix + key, hKey) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }


    /**
     * 生成序号
     */
    public long generate(String key, Long expireSeconds) {
        RedisAtomicLong counter = new RedisAtomicLong(redisPrefix+key, redisTemplate.getConnectionFactory());
        counter.expire(expireSeconds, TimeUnit.SECONDS);
        return counter.incrementAndGet();
    }

    public long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();    //当前日期
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC); //当前秒数
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2.生成序列号
        // 2.1.获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 2.2.自增长
        long count = redisTemplate.opsForValue().increment(keyPrefix + date);

        // 3.拼接并返回 timestamp向左移动32位
        return timestamp << COUNT_BITS | count;
    }
}
