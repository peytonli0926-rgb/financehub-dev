package com.utfinancing.financehub.engine.finance.model.dto;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import com.utfinancing.financehub.engine.utils.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class ConvertTransferOtherPaymentDTO {

    @Excel(name = "合同编号")
    @NotNull(message = "合同编号不能为空")
    private String contractCode;

    @Excel(name = "计划日期", dateFormat = "yyyy-MM-dd")
    @NotNull(message = "计划日期不能为空")
    private String planDate;

    @Excel(name = "计划期数")
    @NotNull(message = "计划期数不能为空")
    private Integer period;

    @Excel(name = "应付租金")
    @NotNull(message = "应付租金不能为空")
    private BigDecimal rentAmount;

    @Excel(name = "应付本金")
    @NotNull(message = "应付本金不能为空")
    private BigDecimal principalAmount;

    @Excel(name = "应付利息")
    @NotNull(message = "应付利息不能为空")
    private BigDecimal interestAmount;

    @Excel(name = "实付日期", dateFormat = "yyyy-MM-dd")
    @NotNull(message = "实付日期不能为空")
    private String paymentDate;

    @Excel(name = "实付租金")
    @NotNull(message = "实付租金不能为空")
    private BigDecimal rentActual;

    @Excel(name = "实付本金")
    @NotNull(message = "实付本金不能为空")
    private BigDecimal principalActual;

    @Excel(name = "实付利息")
    @NotNull(message = "实付利息不能为空")
    private BigDecimal interestActual;

    @Excel(name = "银行账户编码")
    @NotNull(message = "银行账户编码不能为空")
    private String bankAccountCode;

    public ConvertTransferOtherPaymentEntity toEntity() {

        ValidationUtils.validate(this);

        ConvertTransferOtherPaymentEntity entity = new ConvertTransferOtherPaymentEntity();
        entity.setContractCode(contractCode);
        entity.setPlanDate(LocalDate.parse(planDate));
        entity.setPeriod(period);
        entity.setRentAmount(rentAmount);
        entity.setPrincipalAmount(principalAmount);
        entity.setInterestAmount(interestAmount);
        entity.setRentActual(rentActual);
        entity.setPrincipalActual(principalActual);
        entity.setInterestActual(interestActual);
        entity.setBankAccountCode(bankAccountCode);
        entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        entity.setActualDate(LocalDate.parse(paymentDate));
        return entity;
    }
}
