package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : 应付保险费导出
 * @Modified :
 */
@Data
public class PayableInsuranceDetailExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "业务日期", cellType = Excel.ColumnType.DATE)
    private String businessDate;

    @Excel(name = "记账日期", cellType = Excel.ColumnType.DATE)
    private String accountDate;

    @Excel(name = "签约主体")
    private String orgId;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "客户编码")
    private String clientCode;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "业务合同状态")
    private String contractStatus;

    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @Excel(name = "应付保险费-暂估期初余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceEstimateBalanceOpening;

    @Excel(name = "应付保险费-暂估借方", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceEstimateAmountDebit;

    @Excel(name = "应付保险费-暂估贷方", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceEstimateAmountCredit;

    @Excel(name = "应付保险费-暂估期末余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceEstimateBalanceEnding;

//    @Excel(name = "应付保险费余额", cellType = Excel.ColumnType.NUMERIC)
//    private BigDecimal payableInsuranceBalance;

    @Excel(name = "结转金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal carryoverAmount;

    @Excel(name = "保险费支付报表余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceBalanceReport;

    @Excel(name = "保险费实际支付（不含税）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceBalanceActual;

    @Excel(name = "起租时点保险费金额（不含税）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceBalanceLease;

    @Excel(name = "保险费交易结构调整（不含税）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceBalanceStructure;

    @Excel(name = "合同撤销（不含税）", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableInsuranceBalanceWithdrawal;

}
