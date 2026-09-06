package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_repayment_plan_temp")
public class RepaymentPlanTempEntity extends Model<RepaymentPlanTempEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    private String orgId;

    // 日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;
    private Integer planDatePeriod;

    //租金
    private BigDecimal rentAmount;

    //本金
    private BigDecimal principalAmount;

    //利息
    private BigDecimal interestAmount;

    //本金-税金
    private BigDecimal principalTax;

    //利息-税金
    private BigDecimal interestTax;

    //资金流出
    private BigDecimal outflowAmount;

    //计划利息(不含税)
    private BigDecimal plannedInterest;

    //计划本金(不含税)
    private BigDecimal plannedPrincipal;

    //现金流
    private BigDecimal cashFlow;

    //期初摊余成本
    private BigDecimal openingAmortizedCost;

    //期末摊余成本
    private BigDecimal endingAmortizedCost;

    //实际日利率
    private BigDecimal actualDailyRate;

    //租赁收入
    private BigDecimal rentalIncome;

    //表内租赁收入
    private BigDecimal rentalIncomeOnBalance;

    //表外租赁收入
    private BigDecimal rentalIncomeOffBalance;

    //服务费摊销利率
    private BigDecimal serviceFeeAmortizationRate;

    //服务费摊销收入
    private BigDecimal serviceFeeAmortizationIncome;

    //XIRR
    private BigDecimal xirrRate;

    //实际归还日期
    private Date actualRepaymentDate;

    //实际归还本金余额
    private BigDecimal actualRepaymentPrincipalBalance;

    //实际归还本金发生额
    private BigDecimal actualRepaymentPrincipalAmount;

    //实际归还利息余额
    private BigDecimal actualRepaymentInteresBalance;

    //实际归还利息发生额
    private BigDecimal actualRepaymentInteresAmount;

    //回笼状态
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String recaptureStatus;

    //逾期收益
    private BigDecimal overdueEarnings;

    // 备注
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String comment;

    private String messageId;
    private String systemCode;

    // 逾期天数
    private Integer overdueDays;

    // 期数
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private Integer periods;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;

    //还款标识 期初(下还),期末(上还)
    private String payMethod;

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

    //实际归还租金
    private BigDecimal actualRepaymentRentAmount;

    // 人工逾期天数
    private Integer laborOverdueDays;

    // 手工逾期标识
    private String laborOverdueMark;
    // 处理方式
    private String processMethod;
    // 是否观察期
    private String observed;
    // 观察期到期日
    private Date observedExpirationDate;

    // 上次还款日
    private Date lastRepaymentDate;
    // 下次回款日
    private Date nextPaymentDate;
    // 已确认收益
    private BigDecimal confirmedIncome;
    // 实收手续费（含税）(合同维度)
    private BigDecimal paidHandlingFees;
    // 其他收入（含税）(合同维度)
    private BigDecimal otherIncome;
    // 实付其他成本（含税）(合同维度)
    private BigDecimal paidOtherCosts;
    // 开票标识(合同维度)
    private String invoicingFlag;
    // 交易结构手工调整标志
    private String manualChangeMark;

    //是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)
    private String amortized;
    //表内表外(0:表内,1:表外)
    private String onAndOffBalanceSheet;

    //处理状态
    private String processingStatus;

    //序号
    private Long rowNo;


    private BigDecimal sourceIrrRate;

    // 三阶段标识
    private String impairmentThirdStage;
}
