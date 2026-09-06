package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description : 偿还计划测算表-起租-租金计划 对象
 * @Modified :
 */
@Data
public class RepaymentPlanLeaseDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;

    @ApiModelProperty(value = "租金")
    private BigDecimal rentAmount;

    @ApiModelProperty(value = "本金")
    private BigDecimal principalAmount;

    //---------计算
    @ApiModelProperty(value = "利息")
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "本金-税金")
    private BigDecimal principalAmountTaxes;
    @ApiModelProperty(value = "利息-税金")
    private BigDecimal interestAmountTaxes;

    @ApiModelProperty(value = "本金-不含税")
    private BigDecimal principalAmountNoTax;
    @ApiModelProperty(value = "利息-不含税")
    private BigDecimal interestAmountNoTax;


    @ApiModelProperty(value = "现金流")
    private BigDecimal cashFlow;

    @ApiModelProperty(value = "增值税")
    private BigDecimal allTax;


}
