package com.utfinancing.financehub.engine.hthx.common.cache;

import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.hthx.common.base.HthxBaseConfirmData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 应用模块名称: 软提示数据存取服务类
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 20:07
 */


@Component
public class HthxConfirmCacheService<T> {

    @Resource
    private RedisService redisService;

    /**
     * @description: 设置软提示数据
     * @author: zhangli.chen
     **/
    public void setConfirmData(String token, HthxBaseConfirmData data) {
        String key = RedisConstant.WARE_CASH_KEY + token;
        redisService.setCacheObject(key, data, RedisConstant.WARE_TIME_OUT, TimeUnit.MINUTES);
    }

    /**
     * @description: 获取并删除软提示数据
     * @author: zhangli.chen
     **/
    public HthxBaseConfirmData getConfirmData(String token) {
        String key = RedisConstant.WARE_CASH_KEY + token;
        HthxBaseConfirmData data = redisService.getCacheObject(key);
        redisService.deleteObject(key);
        return data;
    }

    /**
     * @description: 设置数据值
     * @author: zhangli.chen
     **/
    public void setData(String token, T data) {
        try {
            String key = RedisConstant.WARE_CASH_KEY + token;
            redisService.setCacheObject(key, data, RedisConstant.WARE_TIME_OUT, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new RuntimeException("Redis set error", e);
        }
    }

    /**
     * @description: 获取数据
     * @author: zhangli.chen
     **/
    public T getData(String token) {
        try{
            String key = RedisConstant.WARE_CASH_KEY + token;
            T data = redisService.getCacheObject(key);
            redisService.deleteObject(key);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Redis get error", e);
        }
    }



}
