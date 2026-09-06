package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description : 应付保险费表详情DTO对象
 * @Modified :
 */
@Data
public class PayableInsuranceDetailsDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "应付保险费表id")
    private Long payableInsuranceId;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "应付保险费-暂估期初余额")
    private BigDecimal payableInsuranceEstimateBalanceOpening;

    @ApiModelProperty(value = "应付保险费-暂估借方")
    private BigDecimal payableInsuranceEstimateAmountDebit;

    @ApiModelProperty(value = "应付保险费-暂估贷方")
    private BigDecimal payableInsuranceEstimateAmountCredit;

    @ApiModelProperty(value = "应付保险费-暂估期末余额")
    private BigDecimal payableInsuranceEstimateBalanceEnding;

    @ApiModelProperty(value = "应付保险费余额")
    private BigDecimal payableInsuranceBalance;

    @ApiModelProperty(value = "结转金额")
    private BigDecimal carryoverAmount;

    @ApiModelProperty(value = "保险费支付报表余额")
    private BigDecimal payableInsuranceBalanceReport;

    @ApiModelProperty(value = "保险费实际支付（不含税）")
    private BigDecimal payableInsuranceBalanceActual;

    @ApiModelProperty(value = "起租时点保险费金额（不含税）")
    private BigDecimal payableInsuranceBalanceLease;

    @ApiModelProperty(value = "保险费交易结构调整（不含税）")
    private BigDecimal payableInsuranceBalanceStructure;

    @ApiModelProperty(value = "合同撤销（不含税）")
    private BigDecimal payableInsuranceBalanceWithdrawal;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "是否删除 0：未删除1：已删除")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
