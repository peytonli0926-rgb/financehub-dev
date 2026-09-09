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
@ApiModel("零售融资租赁回租收到发票事件")
public class RetailLeasebackInvoiceReceiptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请求流水号[orderId]不能为空")
    private String orderId;

    @NotBlank(message = "事件编码[eventCode]不能为空")
    @ApiModelProperty(value = "收到发票、SDFP或发票认证", required = true)
    private String eventCode;

    @NotNull(message = "业务日期[businessDate]不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime businessDate;

    @NotBlank(message = "合同编号[contractCode]不能为空")
    private String contractCode;

    @NotBlank(message = "发票号码[invoiceNumber]不能为空")
    private String invoiceNumber;

    private String invoiceCode;

    @NotNull(message = "不含税金额[untaxedAmount]不能为空")
    @DecimalMin(value = "0.00", message = "不含税金额[untaxedAmount]不能小于0")
    private BigDecimal untaxedAmount;

    @NotNull(message = "税额[taxAmount]不能为空")
    @DecimalMin(value = "0.01", message = "税额[taxAmount]必须大于0")
    private BigDecimal taxAmount;

    @NotNull(message = "价税合计[invoiceAmount]不能为空")
    @DecimalMin(value = "0.01", message = "价税合计[invoiceAmount]必须大于0")
    private BigDecimal invoiceAmount;

    private String currency;
    private String remark;
}
