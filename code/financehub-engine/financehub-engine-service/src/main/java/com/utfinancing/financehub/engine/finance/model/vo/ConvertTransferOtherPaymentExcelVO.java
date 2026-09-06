package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvertTransferOtherPaymentExcelVO {

    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    private String contractCode;

    /**
     * 记账日期
     */
    @Excel(name = "记账日期", dateFormat = "yyyy-MM-dd")
    private LocalDate accountDate;

    /**
     * 计划日期
     */
    @Excel(name = "计划日期", dateFormat = "yyyy-MM-dd")
    private LocalDate planDate;

    /**
     * 计划期数
     */
    @Excel(name = "计划期数")
    private Integer period;

    /**
     * 应付租金
     */
    @Excel(name = "应付租金")
    private BigDecimal rentAmount;

    /**
     * 应付本金
     */
    @Excel(name = "应付本金")
    private BigDecimal principalAmount;

    /**
     * 应付利息
     */
    @Excel(name = "应付利息")
    private BigDecimal interestAmount;

    /**
     * 实付租金
     */
    @Excel(name = "实付租金")
    private BigDecimal rentActual;

    /**
     * 实付本金
     */
    @Excel(name = "实付本金")
    private BigDecimal principalActual;

    /**
     * 实付利息
     */
    @Excel(name = "实付利息")
    private BigDecimal interestActual;

    @Excel(name = "计算扣额")
    private BigDecimal calculateDeductions;

    /**
     * 银行账户编码
     */
    @Excel(name = "银行账户编码")
    private String bankAccountCode;

    /**
     * 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
     */
    @Excel(name = "处理状态", handler = ExcelExportUtil.ProcessStatusExcelHandlerAdapter.class)
    private String processStatus;

    public ConvertTransferOtherPaymentExcelVO(ConvertTransferOtherPaymentEntity entity) {
        this.contractCode = entity.getContractCode();
        this.accountDate = entity.getAccountDate();
        this.planDate = entity.getPlanDate();
        this.period = entity.getPeriod();
        this.rentAmount = entity.getRentAmount();
        this.principalAmount = entity.getPrincipalAmount();
        this.interestAmount = entity.getInterestAmount();
        this.rentActual = entity.getRentActual();
        this.principalActual = entity.getPrincipalActual();
        this.interestActual = entity.getInterestActual();
        this.calculateDeductions = entity.getCalculateDeductions();
        this.bankAccountCode = entity.getBankAccountCode();
        this.processStatus = entity.getProcessStatus();

    }
}
