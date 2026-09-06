package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferThirdPartPaymentExcelVO {
    /**
     * 批次
     */
    @Excel( name = "批次")
    private String batch;

    /**
     * 业务日期
     */
    private LocalDate tradeDate;

    /**
     *  支付主体
     */
    @Excel( name = "支付主体")
    private String paymentOrgId;

    /**
     * 支付金额
     */
    @Excel( name = "支付金额")
    private BigDecimal paymentAmount;

    /**
     * 记账日期
     */
    @Excel( name =  "记账日期", dateFormat = "yyyy-MM-dd")
    private LocalDate accountDate;

    /**
     * 支付日期
     */
    @Excel( name =  "支付日期", dateFormat = "yyyy-MM-dd")
    private LocalDate paymentDate;

    /**
     * 银行账号编码
     */
    @Excel( name = "银行账号编码")
    private String bankAccountCode;

    @Excel( name = "处理状态")
    private String processStatus;

    public static ConvertTransferThirdPartPaymentExcelVO form(ConvertTransferThirdPartPaymentEntity payment) {
        ConvertTransferThirdPartPaymentExcelVO vo = new ConvertTransferThirdPartPaymentExcelVO();
        vo.setBatch(payment.getBatch());
        vo.setTradeDate(payment.getTradeDate());
        vo.setPaymentDate(payment.getPaymentDate());
        vo.setPaymentOrgId(payment.getPaymentOrgId());
        vo.setPaymentAmount(payment.getPaymentAmount());
        vo.setAccountDate(payment.getAccountDate());
        vo.setBankAccountCode(payment.getBankAccountCode());
        vo.setProcessStatus(payment.getProcessStatus());
        return vo;
    }
}
