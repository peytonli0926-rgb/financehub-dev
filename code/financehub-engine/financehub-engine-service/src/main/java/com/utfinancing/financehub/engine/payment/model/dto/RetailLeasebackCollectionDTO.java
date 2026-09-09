package com.utfinancing.financehub.engine.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("零售融资租赁回租收款事件")
public class RetailLeasebackCollectionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空") private String orderId;
    @NotBlank(message = "来源业务事件[eventCode]不能为空")
    @ApiModelProperty(value = "业务系统事件名称或事件编码，例如收取保证金、C001", required = true)
    private String eventCode;
    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") private LocalDateTime businessDate;
    @NotBlank(message = "合同编号[contractCode]不能为空") private String contractCode;

    private String bankAccountNo;
    private String currency;
    private String payerName;
    private String transactionSerial;
    private String remark;

    @DecimalMin(value = "0.00", message = "实际收款金额[receivedAmount]不能小于0") private BigDecimal receivedAmount;
    @DecimalMin(value = "0.00", message = "核销金额[applicationAmount]不能小于0") private BigDecimal applicationAmount;
    @DecimalMin(value = "0.00", message = "现金折扣金额[cashDiscountAmount]不能小于0") private BigDecimal cashDiscountAmount;
    @DecimalMin(value = "0.00", message = "保证金金额[depositAmount]不能小于0") private BigDecimal depositAmount;
    @DecimalMin(value = "0.00", message = "未确认收款金额[unidentifiedAmount]不能小于0") private BigDecimal unidentifiedAmount;
    @DecimalMin(value = "0.00", message = "本金金额[principalAmount]不能小于0") private BigDecimal principalAmount;
    @DecimalMin(value = "0.00", message = "不含税利息[interestAmount]不能小于0") private BigDecimal interestAmount;
    @DecimalMin(value = "0.00", message = "利息增值税[interestTaxAmount]不能小于0") private BigDecimal interestTaxAmount;
    @DecimalMin(value = "0.00", message = "不含税留购价[residualValueAmount]不能小于0") private BigDecimal residualValueAmount;
    @DecimalMin(value = "0.00", message = "留购价增值税[residualValueTaxAmount]不能小于0") private BigDecimal residualValueTaxAmount;
    @DecimalMin(value = "0.00", message = "逾期本金[overduePrincipalAmount]不能小于0") private BigDecimal overduePrincipalAmount;
    @DecimalMin(value = "0.00", message = "逾期不含税利息[overdueInterestAmount]不能小于0") private BigDecimal overdueInterestAmount;
    @DecimalMin(value = "0.00", message = "逾期利息增值税[overdueInterestTaxAmount]不能小于0") private BigDecimal overdueInterestTaxAmount;
    @DecimalMin(value = "0.00", message = "逾期不含税留购价[overdueResidualValueAmount]不能小于0") private BigDecimal overdueResidualValueAmount;
    @DecimalMin(value = "0.00", message = "逾期留购价增值税[overdueResidualValueTaxAmount]不能小于0") private BigDecimal overdueResidualValueTaxAmount;
    @DecimalMin(value = "0.00", message = "退回分润费[profitSharingRefundAmount]不能小于0") private BigDecimal profitSharingRefundAmount;
}
