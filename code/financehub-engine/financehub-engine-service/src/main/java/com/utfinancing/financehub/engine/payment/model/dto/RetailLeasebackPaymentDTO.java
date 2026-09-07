package com.utfinancing.financehub.engine.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 零售融资租赁（回租）付款事件统一入参。
 */
@Data
@ApiModel("零售融资租赁回租付款事件")
public class RetailLeasebackPaymentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空")
    @ApiModelProperty(value = "请求流水号，须全局唯一", required = true)
    private String orderId;

    @NotBlank(message = "事件编码[eventCode]不能为空")
    @ApiModelProperty(value = "零售系统事件编码/名称，例如：购入租赁资产、退回平台合作方提前结清贴息金额", required = true)
    private String eventCode;

    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "业务日期", required = true, example = "2026-09-07 10:00:00")
    private LocalDateTime businessDate;

    @NotBlank(message = "合同编号[contractCode]不能为空")
    @ApiModelProperty(value = "融资租赁合同编号", required = true)
    private String contractCode;

    @NotBlank(message = "客户编码[clientCode]不能为空")
    @ApiModelProperty(value = "客户编码", required = true)
    private String clientCode;

    @NotBlank(message = "客户名称[clientName]不能为空")
    @ApiModelProperty(value = "客户名称", required = true)
    private String clientName;

    @NotBlank(message = "签约主体[orgId]不能为空")
    @ApiModelProperty(value = "签约主体编码", required = true)
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @NotBlank(message = "付款银行账号[bankAccountNo]不能为空")
    @ApiModelProperty(value = "付款或收款银行账号；用于动态取得存放同业/银行存款明细科目", required = true)
    private String bankAccountNo;

    @ApiModelProperty(value = "币种，默认CNY")
    private String currency;

    @ApiModelProperty(value = "支付方式：BANK_TRANSFER或BANK_ACCEPTANCE，默认BANK_TRANSFER")
    private String paymentMethod;

    @ApiModelProperty(value = "资产状态：COMPLETED或CONSTRUCTION，CR003使用，默认COMPLETED")
    private String assetStage;

    @NotNull(message = "付款总额[paymentAmount]不能为空")
    @ApiModelProperty(value = "本事件付款/收款总额；CR025为实际银行支付的含税总额", required = true)
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "不含税费用金额，CR040/CR041使用；CR025由付款总额和业务税率反算")
    private BigDecimal untaxedAmount;

    @ApiModelProperty(value = "不含税利息金额，CR056使用；增值税由该金额乘以税率计算")
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "首付款金额，CR003可选；收到首付款时大于0")
    private BigDecimal initialPaymentAmount;

    @ApiModelProperty(value = "提前结清应退分润费，CR029可选")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "应收款抵扣金额，CR029可选")
    private BigDecimal offsetAmount;

    @ApiModelProperty(value = "银行实付金额，CR029使用；与退回、抵扣金额合计等于付款总额")
    private BigDecimal bankPaymentAmount;

    @ApiModelProperty(value = "收款方编码")
    private String payeeCode;

    @ApiModelProperty(value = "收款方名称")
    private String payeeName;

    @ApiModelProperty(value = "备注")
    private String remark;
}
