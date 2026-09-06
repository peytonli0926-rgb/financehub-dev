package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class ConvertTransferThirdPartPaymentDTO {
    /**
     * 批次
     */
    @Excel(name = "批次")
    private String batch;


    /**
     * 合同编码
     */
    @Excel(name = "合同编码")
    private String contractCode;

    /**
     * 支付主体
     */
    @Excel(name = "支付主体")
    private String paymentOrgId;

    /**
     * 支付金额
     */
    @Excel(name = "支付金额")
    private BigDecimal paymentAmount;


    /**
     * 支付日期
     */
    @Excel(name = "支付日期", dateFormat = "yyyy-MM-dd")
    private String paymentDate;

    /**
     * 银行账号编码
     */
    @Excel(name = "银行账号编码")
    private String bankAccountCode;
}
