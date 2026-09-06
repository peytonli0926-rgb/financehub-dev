package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description : 收益计提导出
 * @Modified :
 */
@Data
public class LeaseIncomeDetailsExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "计提月份", dateFormat = "yyyy-MM-dd")
    private String businessDate;

    @Excel(name = "签约主体")
    private String orgName;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @Excel(name = "合同编号")
    private String contractCode;

//    @Excel(name = "部门")
//    private String contractCreateDept;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务大类")
    private String businessType;

    @Excel(name = "业务类型")
    private String businessName;

    @Excel(name = "业务合同状态")
    private String contractStatus;

    @Excel(name = "币种")
    private String currencyType;

    @Excel(name = "会计起租日")
    private String leaseDateStart;

    @Excel(name = "结束日")
    private String leaseDateEnd;

    @Excel(name = "税率(%)", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal taxRate;

    @Excel(name = "财务IRR", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal xirrRate;
    @Excel(name = "未实现收益总额", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenue;

    @Excel(name = "本期以前", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeBeforeTotal;

    @Excel(name = "本期", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncome;

    @Excel(name = "本期之后", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeAfterTotal;


    @Excel(name = "手工逾期标识")
    private String laborOverdueMark;
//    @Excel(name = "逾期天数")
//    private Integer laborOverdueDays;
    @Excel(name = "系统逾期天数")
    private Integer overdueDays;

    @Excel(name = "上期实收期间")
    private String previousPaidPeriod;

    @Excel(name = "前一月逾期天数")
    private int previousOneMonthOverdueDays;
    @Excel(name = "前二月逾期天数")
    private int previousTwoMonthOverdueDays;
    @Excel(name = "前三月逾期天数")
    private int previousThreeMonthOverdueDays;

    @Excel(name = "逾期收益", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal overdueEarnings;

    @Excel(name = "上月逾期收益", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal lastMonthOverdueEarnings;
    @Excel(name = "当月逾期调整额", cellType=Excel.ColumnType.NUMERIC)
    private String overdueAdjustmentAmount;
    @Excel(name = "合计入账金额", cellType=Excel.ColumnType.NUMERIC)
    private String totalRecordedAmount;
    @Excel(name = "是否观察期")
    private String observed;
    @Excel(name = "观察期到期日")
    private String observedExpirationDate;
    @Excel(name = "处理方式")
    private String processMethod;
    @Excel(name = "备注")
    private String comment;
    @Excel(name = "上次还款日")
    private String lastRepaymentDate;
    @Excel(name = "下次回款日")
    private String nextRepaymentDate;
    @Excel(name = "还款节点")
    private String repaymentNode;
    @Excel(name = "还款规则")
    private String repaymentRule;
    @Excel(name = "实收利息(含税）", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal paidInterest;
    @Excel(name = "已确认收益", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal confirmedIncome;
    @Excel(name = "实收手续费（含税）", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal paidHandlingFees;
    @Excel(name = "其他收入（含税）", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal otherIncome;
    @Excel(name = "实付其他成本（含税）", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal paidOtherCosts;
//    @Excel(name = "TA")
//    private BigDecimal taReclassification;
    @Excel(name = "实收-已确认（不含税）", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal confirmedActualReceipt;
    @Excel(name = "特殊合同状态")
    private String financialContractStatus;
    @Excel(name = "是否转让天津(转让功能)")
    private String transferTJ;
    @Excel(name = "是否博远债转(合同特殊标识)")
    private String transferBoyuanDebt;
    @Excel(name = "手工起租标志(线下合同导入)")
    private String manualLease;
    @Excel(name = "abs赎回标志(转让功能)")
    private String absMark;
    @Excel(name = "交易结构手工调整标志")
    private String manualChangeMark;
    @Excel(name = "异常情况提示")
    private String exceptionType;
    @Excel(name = "还款情况变化提示")
    private String repaymentSituation;
    @Excel(name = "计提方式")
    private String incomeProvisionMethod;
    @Excel(name = "当月是否计提")
    private String accrued;
    @Excel(name = "计提凭证状态")
    private String voucherStatus;
    @Excel(name = "开票标识(合同维度)")
    private String invoicingFlag;
    @Excel(name = "TA金额")
    private String taAmount;

    //表内租赁收入
    @Excel(name = "表内租赁收入", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeOnBalance;

    //表外租赁收入
    @Excel(name = "表外租赁收入", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeOffBalance;

    // 表内转表外金额
    @Excel(name = "表内转表外金额", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal intableTransferOuttableAmount;

    // 表外转表内金额
    @Excel(name = "表外转表内金额", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal outtableTransferIntableAmount;
    @Excel(name = "实收手续费+其他收入", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal paidHandlingFeeAndOtherIncome;
    @Excel(name = "出租人其他成本", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal lessorOtherCosts;
    @Excel(name = "进入观察期账期")
    private Integer enterObservePeriod;
    @Excel(name = "观察期最后还款日", cellType=Excel.ColumnType.DATE)
    private Date enterObserveFinalRepaymentDate;
    @Excel(name = "现金流变化", cellType=Excel.ColumnType.NUMERIC)
    private BigDecimal cashChange;
}
