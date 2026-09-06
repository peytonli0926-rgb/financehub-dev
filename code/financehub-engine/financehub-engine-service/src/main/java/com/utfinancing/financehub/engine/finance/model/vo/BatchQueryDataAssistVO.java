package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : 业务系统对还款认领记录VO对象
 * @Modified :
 */
@Data
public class BatchQueryDataAssistVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "科目本期借方发生额")
    private BigDecimal accountDebitAmount;

    @ApiModelProperty(value = "科目本期贷方发生额")
    private BigDecimal accountCreditAmount;

    @ApiModelProperty(value = "科目期末借方余额")
    private BigDecimal accountEndDebitAmount;

    @ApiModelProperty(value = "科目期贷方方余额")
    private BigDecimal accountEndCreditAmount;
}
