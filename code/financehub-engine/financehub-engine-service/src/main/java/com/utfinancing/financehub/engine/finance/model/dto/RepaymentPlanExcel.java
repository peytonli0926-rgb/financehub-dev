package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RepaymentPlanExcel implements Serializable {

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "计划期数")
    private Integer periods;

    @Excel(name = "计划还款日", cellType = Excel.ColumnType.DATE)
    private Date planDate;

    @Excel(name = "实际还款日", cellType = Excel.ColumnType.DATE)
    private Date actualRepaymentDate;


    @Excel(name = "计划租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentAmount;
    @Excel(name = "计划本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal principalAmount;
    @Excel(name = "计划利息", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal interestAmount;
    @Excel(name = "实收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRepaymentRentAmount;
    @Excel(name = "实收利息", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRepaymentInteresAmount;
    @Excel(name = "实收本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRepaymentPrincipalAmount;
    @Excel(name = "TA重分类", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taReclassification;
    @Excel(name = "资金流出", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal outflowAmount;


    @Excel(name = "摊余成本(期末)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal endingAmortizedCost;
    @Excel(name = "差异", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal differentAmount;
    @Excel(name = "租赁收益", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncome;
    @Excel(name = "分润费分摊额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal profitSharingAllocationAmount;
    @Excel(name = "XIRR(实际利率)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal xirrRate;
    @Excel(name = "回笼情况")
    private String recaptureStatus;
    @Excel(name = "系统逾期天数", cellType = Excel.ColumnType.NUMERIC)
    private Integer overdueDays;
    @Excel(name = "人工逾期天数", cellType = Excel.ColumnType.NUMERIC)
    private Integer laborOverdueDays;
    @Excel(name = "逾期收益", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal overdueEarnings;
    @Excel(name = "已确认逾期收益(表外)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeOnBalanceConfirmed;
    @Excel(name = "累计实收利息（不含税）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accumulatedActualRepaymentInteresAmount;
    @Excel(name = "实收手续费+实收其他收入-实付成本", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal paidInHandlingFeesAddOtherIncomeSubCosts;
    @Excel(name = "已确认租赁收益(表内)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentalIncomeOffBalanceConfirmed;

}
