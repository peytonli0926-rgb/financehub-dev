package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferOtherPaymentVO {

    private Long id;
    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String contractCode;

    /**
     * 记账日期
     */
    @ApiModelProperty("记账日期")
    private LocalDate accountDate;
    /**
     * 实付日期
     */
    private LocalDate actualDate;

    /**
     * 计划日期
     */
    @ApiModelProperty("计划日期")
    private LocalDate planDate;

    /**
     * 计划期数
     */
    @ApiModelProperty("计划期数")
    private Integer period;

    /**
     * 应付租金
     */
    @ApiModelProperty("应付租金")
    private BigDecimal rentAmount;

    /**
     * 应付本金
     */
    @ApiModelProperty("应付本金")
    private BigDecimal principalAmount;

    /**
     * 应付利息
     */
    @ApiModelProperty("应付利息")
    private BigDecimal interestAmount;

    /**
     * 实付租金
     */
    @ApiModelProperty("实付租金")
    private BigDecimal rentActual;

    /**
     * 实付本金
     */
    @ApiModelProperty("实付本金")
    private BigDecimal principalActual;

    /**
     * 实付利息
     */
    @ApiModelProperty("实付利息")
    private BigDecimal interestActual;

    /**
     * 计算扣额
     */
    @ApiModelProperty("计算扣额")
    private BigDecimal calculateDeductions;
    /**
     * 银行账户编码
     */
    @ApiModelProperty("银行账户编码")
    private String bankAccountCode;

    /**
     * 流程id
     */
    @ApiModelProperty("流程id")
    private Long processInstanceId;

    /**
     * 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
     */
    @ApiModelProperty("处理状态 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    /**
     * 是否已生成凭证（0：未生成1：已生成）默认0
     */
    @ApiModelProperty("是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    /**
     * 凭证id,多个按照逗号分隔
     */
    @ApiModelProperty("凭证id,多个按照逗号分隔")
    private String voucherId;

    /**
     * 生成凭证报错信息
     */
    @ApiModelProperty("生成凭证报错信息")
    private String errorInfo;

    public ConvertTransferOtherPaymentVO(ConvertTransferOtherPaymentEntity entity) {

        this.id = entity.getId();
        this.contractCode = entity.getContractCode();

        this.accountDate = entity.getAccountDate();
        this.actualDate = entity.getActualDate();
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

        this.processInstanceId = entity.getProcessInstanceId();
        this.processStatus = entity.getProcessStatus();

        this.isGenerateVoucher = entity.getIsGenerateVoucher();
        this.voucherId = entity.getVoucherId();
        this.errorInfo = entity.getErrorInfo();

    }
}
