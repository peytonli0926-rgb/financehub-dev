package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class HyFullOnlineBankBatchNoMappingDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "业务系统网银编号")
    private String onlineBankNo;

    @ApiModelProperty(value = "批扣流水号")
    private String deductBatchNo;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0:否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "扣除种类:1手动汇款、2自动扣款")
    private String deductCategory;

    @ApiModelProperty(value = "乘用、商用")
    private String businessLine;

    @ApiModelProperty(value = "银行回单号")
    private String bankReceiptNo;

    @ApiModelProperty(value = "到账时间")
    private LocalDateTime collectAmountTime;

    @ApiModelProperty(value = "到账金额")
    private String collectAmount;

    @ApiModelProperty(value = "到账银行")
    private String collectBank;

    @ApiModelProperty(value = "到账银行账号")
    private String collectBankAccount;

    @ApiModelProperty(value = "摘要")
    private String comments;

    @ApiModelProperty(value = "付款账户")
    private String payBankAccountName;

    @ApiModelProperty(value = "付款银行")
    private String payBank;

    @ApiModelProperty(value = "付款银行账户")
    private String payBankAccount;

    @ApiModelProperty(value = "可核销金额")
    private String canChargeOffAmount;

    @ApiModelProperty(value = "已核销金额")
    private String alreadyChargeOffAmount;

    @ApiModelProperty(value = "已认领溢存款待核销金额。认领到溢存款时增加，溢存款出账时减少")
    private String decDongjje;

    @ApiModelProperty(value = "退款金额")
    private String refundAmount;

    @ApiModelProperty(value = "到账账户")
    private String collectAmountAccountName;

    @ApiModelProperty(value = "退款标志")
    private String refundFlag;

    @ApiModelProperty(value = "网银归属 01-C0001:恒信 30001:自贸区")
    private String onlineBankBelong;

    @ApiModelProperty("客户编码")
    private String clientCode;

    @ApiModelProperty("客户名称")
    private String clientName;

}
