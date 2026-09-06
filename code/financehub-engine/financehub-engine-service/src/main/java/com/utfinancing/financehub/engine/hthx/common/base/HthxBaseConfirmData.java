package com.utfinancing.financehub.engine.hthx.common.base;

import com.utfinancing.financehub.engine.constants.RedisConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用模块名称:
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/4/14 20:00
 */

@ToString
@Data
@AllArgsConstructor
public class HthxBaseConfirmData implements Serializable {

    /**
     * @description: 唯一标识
     **/
    private String confirmKey;

    /**
     * @description: 原始数据Hash
     **/
    private int dataHash;

    /**
     * @description: 过期时间
     **/
    private LocalDateTime expireTime;

    /**
     * @description: 构造器，默认失效时长5分钟-支持校验用户数据防篡改
     **/
    public HthxBaseConfirmData(String confirmKey, int dataHash) {
        this.confirmKey = confirmKey;
        this.dataHash = dataHash;
        this.expireTime = LocalDateTime.now().plusMinutes(RedisConstant.WARE_TIME_OUT);
    }

    /**
     * @description: 构造器，默认失效时长5分钟-仅支持校验二次确认key是否失效
     **/
    public HthxBaseConfirmData(String confirmKey) {
        this.confirmKey = confirmKey;
        this.expireTime = LocalDateTime.now().plusMinutes(RedisConstant.WARE_TIME_OUT);
    }

}
