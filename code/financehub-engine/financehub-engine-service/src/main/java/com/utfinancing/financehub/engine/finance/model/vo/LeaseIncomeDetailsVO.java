package com.utfinancing.financehub.engine.finance.model.vo;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-13
 * @Description : VO对象
 * @Modified :
 */
@Data
public class LeaseIncomeDetailsVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "计提月份")
    private String businessDate;

    private String provisionMonth;

    @ApiModelProperty(value = "记账日期")
    private String accountDate;

    @ApiModelProperty(value = "租赁收益表id")
    private Long leaseIncomeId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "业务大类")
    private String businessType;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "财务IRR")
    private BigDecimal xirrRate;

    @ApiModelProperty(value = "未实现收益总额")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "本月以前")
    private BigDecimal rentalIncomeBeforeTotal;

    @ApiModelProperty(value = "本月")
    private BigDecimal rentalIncome;

    @ApiModelProperty(value = "本月之后")
    private BigDecimal rentalIncomeAfterTotal;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "逾期收益")
    private BigDecimal overdueEarnings;

    @ApiModelProperty(value = "上月逾期收益")
    private BigDecimal lastMonthOverdueEarnings;

    @ApiModelProperty(value = "起租日")
    private String leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private String leaseDateEnd;

    @ApiModelProperty(value = "上期实收期间")
    private String previousPaidPeriod;

    @ApiModelProperty(value = "当月逾期调整额")
    private BigDecimal overdueAdjustmentAmount;

    @ApiModelProperty(value = "合计入账金额")
    private BigDecimal totalRecordedAmount;

    @ApiModelProperty(value = "实收-已确认")
    private BigDecimal confirmedActualReceipt;

    @ApiModelProperty(value = "是否计提")
    private String accrued;

    @ApiModelProperty(value = "计提方式")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "凭证id")
    private String voucherId;

    @ApiModelProperty(value = "是否删除 0：未删除1：已删除")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Excel(name = "计提凭证状态")
    private String voucherStatus;

    // 0:非手工起租 1:手工起租
    @ApiModelProperty(value = "0:非手工起租 1:手工起租")
    private String manualLeaseFlag;

    // 交易结构手工调整标志
    @ApiModelProperty(value = "交易结构手工调整标志")
    private String manualChangeMark;

    // 观察期到期日
    @ApiModelProperty(value = "观察期到期日")
    private String observedExpirationDate;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "上次还款日")
    private String lastRepaymentDate;
    @ApiModelProperty(value = "下次还款日")
    private String nextRepaymentDate;

    @ApiModelProperty(value = "合同特殊标识")
    private String specialFlag;
    @ApiModelProperty(value = "TA金额")
    private String taAmount;

    @ApiModelProperty(value = "还款节点")
    private String repaymentNode;
    @ApiModelProperty(value = "还款规则")
    private String repaymentRule;

    // 是否观察期
    @ApiModelProperty(value = "是否观察期")
    private String observed;

    @ApiModelProperty(value = "历史逾期")
    private String historyOverdue;

    @ApiModelProperty(value = "前一月逾期天数")
    private int previousOneMonthOverdueDays;
    @ApiModelProperty(value = "前二月逾期天数")
    private int previousTwoMonthOverdueDays;
    @ApiModelProperty(value = "前三月逾期天数")
    private int previousThreeMonthOverdueDays;

    @ApiModelProperty(value = "实收利息(含税）")
    private BigDecimal paidInterest;
    @ApiModelProperty(value = "已确认收益")
    private BigDecimal confirmedIncome;
    @ApiModelProperty(value = "实收手续费（含税）")
    private BigDecimal paidHandlingFees;
    @ApiModelProperty(value = "其他收入（含税）")
    private BigDecimal otherIncome;
    @ApiModelProperty(value = "是否博远债转(合同特殊标识)")
    private String transferBoyuanDebt;

    //表内租赁收入
    @ApiModelProperty(value = "表内租赁收入")
    private BigDecimal rentalIncomeOnBalance;

    //表外租赁收入
    @ApiModelProperty(value = "表外租赁收入")
    private BigDecimal rentalIncomeOffBalance;

    // 表内转表外金额
    @ApiModelProperty(value = "表内转表外金额")
    private BigDecimal intableTransferOuttableAmount;


    // 表外转表内金额
    @ApiModelProperty(value = "表外转表内金额")
    private BigDecimal outtableTransferIntableAmount;

    @ApiModelProperty(value = "实收手续费（含税）和其他收入（含税）")
    private BigDecimal paidHandlingFeeAndOtherIncome;
    @ApiModelProperty(value = "出租人其他成本")
    private BigDecimal lessorOtherCosts;
    @ApiModelProperty(value = "进入观察期账期")
    private Integer enterObservePeriod;
    @ApiModelProperty(value = "观察期最后还款日")
    private Date enterObserveFinalRepaymentDate;

    @ApiModelProperty(value = "现金流变化")
    private BigDecimal cashChange;
}
