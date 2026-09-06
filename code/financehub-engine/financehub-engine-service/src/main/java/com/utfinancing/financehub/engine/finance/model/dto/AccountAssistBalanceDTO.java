package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description : 科目辅助帐余额表DTO对象
 * @Modified :
 */
@Data
public class AccountAssistBalanceDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "业务类型")
    private String businessCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "年初借方余额")
    private BigDecimal yearBeginDebitBalance;

    @ApiModelProperty(value = "年初贷方余额")
    private BigDecimal yearBeginCreditBalance;

    @ApiModelProperty(value = "期初借方余额")
    private BigDecimal monthBeginDebitBalance;

    @ApiModelProperty(value = "期初贷方余额")
    private BigDecimal monthBeginCreditBalance;

    @ApiModelProperty(value = "本期借方发生额")
    private BigDecimal monthDebitAmount;

    @ApiModelProperty(value = "本期贷方发生额")
    private BigDecimal monthCreditAmount;

    @ApiModelProperty(value = "年累计借方发生额")
    private BigDecimal yearDebitAmount;

    @ApiModelProperty(value = "年累计贷方发生额")
    private BigDecimal yearCreditAmount;

    @ApiModelProperty(value = "期末借方余额")
    private BigDecimal monthEndDebitBalance;

    @ApiModelProperty(value = "期末贷方余额")
    private BigDecimal monthEndCreditBalance;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
