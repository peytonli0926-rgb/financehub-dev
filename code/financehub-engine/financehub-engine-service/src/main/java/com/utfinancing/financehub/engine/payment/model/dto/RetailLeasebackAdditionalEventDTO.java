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

@Data
@ApiModel("零售融资租赁回租补充业务事件")
public class RetailLeasebackAdditionalEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空") private String orderId;
    @NotBlank(message = "来源业务事件[eventCode]不能为空")
    @ApiModelProperty(value = "业务系统事件名称或编码", required = true)
    private String eventCode;
    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") private LocalDateTime businessDate;
    @NotBlank(message = "合同编号[contractCode]不能为空") private String contractCode;

    private String bankAccountNo;
    private String currency;
    private String counterpartyName;
    private String transactionSerial;
    private String changeReason;
    private String remark;

    private BigDecimal refundAmount;
    private BigDecimal depositAmount;
    private BigDecimal advanceReceiptAmount;
    private BigDecimal unidentifiedAmount;

    private BigDecimal subsidyAmount;
    private BigDecimal subsidyTaxAmount;

    private BigDecimal overduePrincipalAmount;
    private BigDecimal overdueInterestAmount;
    private BigDecimal overdueInterestTaxAmount;
    private BigDecimal overdueResidualValueAmount;
    private BigDecimal overdueResidualValueTaxAmount;
    private BigDecimal penaltyInterestAmount;
    private BigDecimal penaltyInterestTaxAmount;

    /** 交易结构变更金额为有符号差额：正数调增，负数调减。 */
    private BigDecimal principalAdjustmentAmount;
    private BigDecimal interestAdjustmentAmount;
    private BigDecimal interestTaxAdjustmentAmount;
    private BigDecimal residualValueAdjustmentAmount;
    private BigDecimal residualValueTaxAdjustmentAmount;
    private BigDecimal gpsAdjustmentAmount;

    // 辅助账调整：同一金额类型只保留一个有符号字段。
    private BigDecimal principalBalance;
    private BigDecimal interestBalance;
    private BigDecimal residualValueBalance;
    private BigDecimal interestTaxBalance;
    private BigDecimal residualValueTaxBalance;
    private BigDecimal accruedInterestBalance;
    private BigDecimal accruedResidualValueBalance;
    private BigDecimal accruedInterestTaxBalance;
    private BigDecimal accruedResidualValueTaxBalance;
    private BigDecimal unidentifiedReceiptAssistAmount;
    private BigDecimal managementFeePayableAssistAmount;
    private BigDecimal inputVatReceivableAssistAmount;
    private BigDecimal unearnedInterestAssistAmount;
    private String originalClientCode;
    private String originalClientName;

    // 其他场景。
    private String sourceBankAccountNo;
    private String targetBankAccountNo;
    private BigDecimal transferAmount;
    private BigDecimal quarterlyInterestAmount;
    private BigDecimal overpaidProfitSharingAmount;
    private BigDecimal channelShareAmount;
    private BigDecimal penaltyShareAdjustmentAmount;
    private BigDecimal promotionInterestAmount;
    private BigDecimal promotionInputVatAmount;
    private BigDecimal promotionIncomeAmount;
    private BigDecimal promotionProfitSharingAmount;
}
