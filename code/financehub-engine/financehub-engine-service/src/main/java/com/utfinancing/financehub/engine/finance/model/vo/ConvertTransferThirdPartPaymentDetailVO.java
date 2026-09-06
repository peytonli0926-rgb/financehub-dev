package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferThirdPartPaymentDetailVO {
    /**
     * 批次
     */
    @ApiModelProperty("批次")
    private String batch;

    /**
     * 合同编码
     */
    @ApiModelProperty("合同编码")
    private String contractCode;

    /**
     *  支付主体
     */
    @ApiModelProperty("支付主体")
    private String paymentOrgId;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal paymentAmount;

    /**
     * 记账日期
     */
    @ApiModelProperty(value = "记账日期", example = "2024-12-20")
    private LocalDate tradeDate;

    /**
     * 记账日期
     */
    @ApiModelProperty(value = "记账日期", example = "2024-12-20")
    private LocalDate accountDate;

    /**
     * 支付日期
     */
    @ApiModelProperty(value = "支付日期",example = "2024-12-20")
    private LocalDate paymentDate;

    /**
     * 银行账号编码
     */
    @ApiModelProperty("银行账号编码")
    private String bankAccountCode;

    public static ConvertTransferThirdPartPaymentDetailVO from(ConvertTransferThirdPartPaymentDetailEntity entity) {
        ConvertTransferThirdPartPaymentDetailVO vo = new ConvertTransferThirdPartPaymentDetailVO();
        vo.setBatch(entity.getBatch());
        vo.setContractCode(entity.getContractCode());
        vo.setPaymentOrgId(entity.getPaymentOrgId());
        vo.setTradeDate(entity.getTradeDate());
        vo.setAccountDate(entity.getAccountDate());
        vo.setPaymentDate(entity.getPaymentDate());
        vo.setBankAccountCode(entity.getBankAccountCode());
        vo.setPaymentAmount(entity.getPaymentAmount());
        return vo;
    }
}
