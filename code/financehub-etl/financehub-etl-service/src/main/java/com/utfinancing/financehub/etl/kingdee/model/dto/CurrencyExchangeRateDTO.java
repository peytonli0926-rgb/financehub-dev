package com.utfinancing.financehub.etl.kingdee.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : VO对象
 * @Modified :
 */
@Data
public class CurrencyExchangeRateDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "查询日期")
    private String queryDate;

    @ApiModelProperty(value = "来源币别代码")
    private String sourceEasCode;

    @ApiModelProperty(value = "来源币别名称")
    private String sourceEasName;

    @ApiModelProperty(value = "目标币别代码")
    private String targetEasCode;

    @ApiModelProperty(value = "目标币别名称")
    private String targetEasName;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

}
