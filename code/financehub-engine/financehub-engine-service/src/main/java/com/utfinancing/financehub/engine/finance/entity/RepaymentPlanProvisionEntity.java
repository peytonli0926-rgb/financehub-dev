package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 偿还计划测算表-计提用实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_repayment_plan_provision")
public class RepaymentPlanProvisionEntity extends Model<RepaymentPlanProvisionEntity> {

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

    //日期
    private LocalDate planDate;

    //租金
    private String rentAmount;

    //本金
    private String principalAmount;

    //利息
    private String interestAmount;

    //本金-税金
    private String principalTax;

    //利息-税金
    private String interestTax;

    //资金流出
    private String outflowAmount;

    //计划利息(不含税)
    private String plannedInterest;

    //计划本金(不含税)
    private String plannedPrincipal;

    //现金流
    private String cashFlow;

    //期初摊余成本
    private String openingAmortizedCost;

    //期末摊余成本
    private String endingAmortizedCost;

    //实际日利率
    private String actualDailyRate;

    //租赁收入
    private String rentalIncome;

    //服务费摊销利率
    private String serviceFeeAmortizationRate;

    //服务费摊销收入
    private String serviceFeeAmortizationIncome;

    //XIRR
    private String xirrRate;

    //实际归还日期
    private LocalDate actualRepaymentDate;

    //实际归还本金余额
    private String actualRepaymentPrincipalBalance;

    //实际归还本金发生额
    private String actualRepaymentPrincipalAmount;

    //实际归还利息余额
    private String actualRepaymentInteresBalance;

    //实际归还利息发生额
    private String actualRepaymentInteresAmount;

    //回笼状态
    private String recaptureStatus;

    //逾期收益
    private String overdueEarnings;

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
    private String delFlag;

    //备注
    private String comment;

    //逾期天数
    private Integer overdueDays;

    //期数
    private Integer periods;

    //还款标识 期初(下还),期末(上还)
    private String payMethod;

    private String messageId;

    private String systemCode;

    //表内租赁收入
    private String rentalIncomeOnBalance;

    //表外租赁收入
    private String rentalIncomeOffBalance;

    //分摊方式(租赁收入分摊、服务费收入分摊)
    private String allocationMethod;

    // 分摊比例
    private String allocationRatio;

    //服务费实收（税后）
    private String serviceFeeReceived;

    //应分摊的服务费收入（税后）
    private String serviceFeeAllocationNoTax;

    //上月服务费应分摊金额（税后）
    private String lastMonthServiceFeeAllocationNoTax;

    //本月重分类调整(税后)
    private String reclassificationAdjustmentNoTaxAmount;

    //X年X月调整
    private String xYearMonthAdjustmentAmount;

    //实际未计提金额
    private String notAccruedAmount;

    //累计已计提金额
    private String accumulatedAccruedAmount;

    // 累计实收利息（不含税）
    private String accumulatedActualRepaymentInteresAmount;

    // 实收手续费+实收其他收入-实付成本
    private String paidInHandlingFeesAddOtherIncomeSubCosts;

    //TA重分类
    private String taReclassification;

    //未实现收益总额
    private String unrealizedRevenue;

    //上期实收期间
    private LocalDateTime previousPaidPeriod;

    //本月以前
    private String rentalIncomeBeforeTotal;

    //本月之后
    private String rentalIncomeAfterTotal;

    //当月逾期调整额
    private String overdueAdjustmentAmount;

    //合计入账金额
    private String totalRecordedAmount;

    //实收-已确认
    private String confirmedActualReceipt;

    //是否计提
    private String accrued;

    //计提方式
    private String incomeProvisionMethod;

    //异常类型
    private String exceptionType;

    // 已确认逾期收益(表外)
    private String rentalIncomeOnBalanceConfirmed;

    // 已确认租赁收益(表内)
    private String rentalIncomeOffBalanceConfirmed;

    //实际归还租金
    private String actualRepaymentRentAmount;

    // 人工逾期天数
    private Integer laborOverdueDays;

    //X年X月以前
    private String beforeXYearMonthAmount;

    //X年X月摊销后余额
    private String allocationAfterXYearMonthBalance;

    //签约主体
    private String orgId;

    //手工逾期标识
    private String laborOverdueMark;

    //处理方式
    private String processMethod;

    //是否观察期
    private String observed;

    //观察期到期日
    private LocalDateTime observedExpirationDate;

    //上次还款日
    private LocalDateTime lastRepaymentDate;

    //下次回款日
    private LocalDateTime nextPaymentDate;

    //已确认收益
    private String confirmedIncome;

    //实收手续费
    private String paidHandlingFees;

    //其他收入
    private String otherIncome;

    //实付其他成本
    private String paidOtherCosts;

    //开票标识
    private String invoicingFlag;

    //交易结构手工调整标志
    private String manualChangeMark;

    //计划还款日(yyyyMM)
    private Integer planDatePeriod;

    //是否已摊销(是否计提为是,且计提则为已摊销,0:未摊销,1:已摊销)
    private String amortized;

    //表内表外(0:表内,1:表外)
    private String onAndOffBalanceSheet;

    //源IRR Rate
    private String sourceIrrRate;

    // 三阶段标识
    private String impairmentThirdStage;
}
