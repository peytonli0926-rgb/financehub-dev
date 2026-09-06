package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description : 偿还计划测算表DTO对象
 * @Modified :
 */
@Data
public class RepaymentPlanSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;

    @ApiModelProperty(value = "租金")
    private BigDecimal rentAmount;

    @ApiModelProperty(value = "本金")
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "利息")
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "本金-税金")
    private BigDecimal principalTax;

    @ApiModelProperty(value = "利息-税金")
    private BigDecimal interestTax;

    @ApiModelProperty(value = "资金流出")
    private BigDecimal outflowAmount;

    @ApiModelProperty(value = "计划利息(不含税)")
    private BigDecimal plannedInterest;

    @ApiModelProperty(value = "计划本金(不含税)")
    private BigDecimal plannedPrincipal;

    @ApiModelProperty(value = "现金流")
    private BigDecimal cashFlow;

    @ApiModelProperty(value = "期初摊余成本")
    private BigDecimal openingAmortizedCost;

    @ApiModelProperty(value = "期末摊余成本")
    private BigDecimal endingAmortizedCost;

    @ApiModelProperty(value = "实际日利率")
    private BigDecimal actualDailyRate;

    @ApiModelProperty(value = "租赁收入")
    private BigDecimal rentalIncome;

    @ApiModelProperty(value = "表内租赁收入")
    private BigDecimal rentalIncomeOnBalance;

    @ApiModelProperty(value = "表外租赁收入")
    private BigDecimal rentalIncomeOffBalance;

    @ApiModelProperty(value = "服务费摊销利率")
    private BigDecimal serviceFeeAmortizationRate;

    @ApiModelProperty(value = "服务费摊销收入")
    private BigDecimal serviceFeeAmortizationIncome;

    @ApiModelProperty(value = "XIRR")
    private BigDecimal xirrRate;

    @ApiModelProperty(value = "实际归还日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualRepaymentDate;


    @ApiModelProperty(value = "实际归还本金")
    private BigDecimal actualRepaymentPrincipalAmount;
    private BigDecimal actualRepaymentPrincipalBalance;


    @ApiModelProperty(value = "实际归还利息")
    private BigDecimal actualRepaymentInteresAmount;
    private BigDecimal actualRepaymentInteresBalance;

    @ApiModelProperty(value = "回笼状态")
    private String recaptureStatus;

    @ApiModelProperty(value = "逾期收益")
    private BigDecimal overdueEarnings;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "期数")
    private Integer periods;

    private String systemCode;
    private String orgId;

    // 服务费分摊部分
    //分摊方式(租赁收入分摊、服务费收入分摊)
    private String allocationMethod;

    // 分摊比例
    private BigDecimal allocationRatio;

    //服务费实收（税后）
    private BigDecimal serviceFeeReceived;

    //应分摊的服务费收入（税后）
    private BigDecimal serviceFeeAllocationNoTax;

    //上月服务费应分摊金额（税后）
    private BigDecimal lastMonthServiceFeeAllocationNoTax;

    //本月重分类调整(税后)
    private BigDecimal reclassificationAdjustmentNoTaxAmount;

    //本期以前
    private BigDecimal beforeXYearMonthAmount;

    //本期调整
    private BigDecimal xYearMonthAdjustmentAmount;

    //本期摊销后余额
    private BigDecimal allocationAfterXYearMonthBalance;

    //实际未计提金额
    private BigDecimal notAccruedAmount;

    //累计已计提金额
    private BigDecimal accumulatedAccruedAmount;

    // 收益计提部分
    // 累计实收利息（不含税）
    private BigDecimal accumulatedActualRepaymentInteresAmount;
    // 实收手续费+实收其他收入-实付成本
    private BigDecimal paidInHandlingFeesAddOtherIncomeSubCosts;
    //TA重分类
    private BigDecimal taReclassification;
    //未实现收益总额
    private BigDecimal unrealizedRevenue;

    //上期实收期间
    private Date previousPaidPeriod;

    //本月以前
    private BigDecimal rentalIncomeBeforeTotal;

    //本月之后
    private BigDecimal rentalIncomeAfterTotal;

    //当月逾期调整额
    private BigDecimal overdueAdjustmentAmount;

    //合计入账金额
    private BigDecimal totalRecordedAmount;

    //实收-已确认
    private BigDecimal confirmedActualReceipt;

    //是否计提
    private String accrued;

    //计提方式
    private String incomeProvisionMethod;

    //异常类型
    private String exceptionType;

    // 已确认逾期收益(表外)
    private BigDecimal rentalIncomeOnBalanceConfirmed;

    // 已确认租赁收益(表内)
    private BigDecimal rentalIncomeOffBalanceConfirmed;

    //实际归还本金
    private BigDecimal actualRepaymentRentAmount;

    // 人工逾期天数
    private Integer laborOverdueDays;

    private String messageId;
    //还款标识 期初(下还),期末(上还)
    private String payMethod;

    private Date changeDate;

    // 是否观察期
    private String observed;
    @ApiModelProperty(value = "观察期到期日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date observedExpirationDate;
    @ApiModelProperty(value = "差异")
    private BigDecimal differentAmount;
    // 计算irr利息标识
    private Boolean irrMark;

    @ApiModelProperty("网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("结算方式")
    private String settlementWay;
    private Integer planDatePeriod;
    private BigDecimal sourceIrrRate;


    // 调整金额
    private BigDecimal adjustmentAmount;

    // 变更后期末摊余成本
    private BigDecimal changeAfterEndingAmortizedCost;

    // 变更后租赁收入
    private BigDecimal changeAfterRentalIncome;
    private String manualChangeMark;

    /** Whether the period has already been amortized (0=no, 1=yes). */
    private String amortized;

    /** Income recognition position (0=on balance sheet, 1=off balance sheet). */
    private String onAndOffBalanceSheet;

    /** Whether the contract is in impairment stage three. */
    private String impairmentThirdStage;

    private String delFlag;
}
