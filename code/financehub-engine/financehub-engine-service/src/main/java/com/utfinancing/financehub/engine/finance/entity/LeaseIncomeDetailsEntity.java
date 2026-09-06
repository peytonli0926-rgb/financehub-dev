package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_lease_income_details")
public class LeaseIncomeDetailsEntity extends Model<LeaseIncomeDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //计提月份
    private Date businessDate;

    //记账日期
    private Date accountDate;

    //租赁收益表id
    private Long leaseIncomeId;

    //来源系统
    private String systemCode;

    //合同编号
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //业务大类
    private String businessType;

    //业务类型编码
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String businessCode;

    //业务类型名称
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String businessName;

    //业务合同状态
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String contractStatus;

    //币种
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String currencyType;

    //税率
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal taxRate;

    //财务IRR
    private BigDecimal xirrRate;

    //未实现收益总额
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal unrealizedRevenue;

    //本月以前
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal rentalIncomeBeforeTotal;

    //本月
    private BigDecimal rentalIncome;

    //本月之后
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal rentalIncomeAfterTotal;

    //逾期天数
    private Integer overdueDays;

    //逾期收益
    private BigDecimal overdueEarnings;

    // 上月逾期收益
    private BigDecimal lastMonthOverdueEarnings;


    //起租日
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private Date leaseDateStart;

    //到期日
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private Date leaseDateEnd;

    //上期实收期间
    private Date previousPaidPeriod;

    //表内租赁收入
    private BigDecimal rentalIncomeOnBalance;

    //表外租赁收入
    private BigDecimal rentalIncomeOffBalance;

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

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //异常类型
    private String exceptionType;

    //开票标识
    private String invoicingFlag;

    //凭证id
    private String voucherId;

    //是否删除 0：未删除1：已删除
    private String delFlag;

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

    // 是否观察期
    private String observed;
    // 观察期到期日
    private Date observedExpirationDate;
    // 备注
    private String comment;
    // 处理方式
    private String processMethod;
    // 人工逾期天数
    private Integer laborOverdueDays;
    // 手工逾期标识
    private String laborOverdueMark;

    // 表内转表外金额
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal intableTransferOuttableAmount;


    // 表外转表内金额
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private BigDecimal outtableTransferIntableAmount;

    // 交易结构手工调整标志
    private String manualChangeMark;

    private BigDecimal taAmount;
    private Date lastRepaymentDate;
    private Date nextRepaymentDate;
    // 历史逾期
    private String historyOverdue;

    // 实收利息(不含税）
    private BigDecimal paidInterest;
    // 已确认收益
    private BigDecimal confirmedIncome;

    // 实收手续费（不含税）
    private BigDecimal paidHandlingFees;

    // 其它收入(不含税)
    private BigDecimal otherIncome;

    // 特殊合同状态
    private String financialContractStatus;
    // 现金流变化
    private BigDecimal cashChange;
    //出租人其他成本
    private BigDecimal lessorOtherCosts;
    //进入观察期账期
    private Integer enterObservePeriod;
    //观察期最后还款日
    private Date enterObserveFinalRepaymentDate;
}
