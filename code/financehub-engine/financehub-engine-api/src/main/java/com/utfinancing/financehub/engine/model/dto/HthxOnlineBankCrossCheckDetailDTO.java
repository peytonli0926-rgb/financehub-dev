package com.utfinancing.financehub.engine.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/11 19:22
 */
@ToString
@Data
public class HthxOnlineBankCrossCheckDetailDTO implements Serializable {

    /**
     * @description: 勾稽编号
     **/
     String gjbh;

    /**
     * @description: 网银金额
     **/
    private BigDecimal gjje;

    /**
     * @description: 资金网银编号
     **/
    String zjwybh;

    /**
     * @description: 恒运大网银编号
     **/
    String pcwybh;




}
