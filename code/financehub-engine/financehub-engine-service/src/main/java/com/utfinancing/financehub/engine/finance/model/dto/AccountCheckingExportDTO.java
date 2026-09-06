package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountCheckingExportDTO implements Serializable {

    @Excel(name = "对账月份")
    private String accountCheckingMonth;

    @Excel(name = "所属公司")
    private String collectionAccountsBank;

    @Excel(name = "业务系统")
    private String systemCode;

    @Excel(name = "业务系统网银编号")
    private String businessEbankNumber;

    @Excel(name = "业务系统网银编号-小网银")
    private String ebankSerialNumber;

    @Excel(name = "入账日期", cellType = Excel.ColumnType.DATE)
    private Date businessHappenDate;

    @Excel(name = "账龄", cellType = Excel.ColumnType.NUMERIC)
    private String accountAge;

    //账龄分类
    @Excel(name = "账龄分类")
    private String accountAgeClass;

    @Excel(name = "付款客户")
    private String clientName;

    //月初余额
    @Excel(name = "本月初余额", cellType = Excel.ColumnType.NUMERIC)
    private String monthInitBalance;

    //月初余额
    @Excel(name = "本月贷方发生额", cellType = Excel.ColumnType.NUMERIC)
    private String curMonthCreditAmount;

    //本月余额
    @Excel(name = "本月余额", cellType = Excel.ColumnType.NUMERIC)
    private String curMonthBalance;

    //系统金额
    @Excel(name = "系统金额", cellType = Excel.ColumnType.NUMERIC)
    private String systemAmount;

    //差额
    @Excel(name = "差额", cellType = Excel.ColumnType.NUMERIC)
    private String diffAmount;

    //财务对账备注
    @Excel(name = "财务对账备注")
    private String accountCheckingComments;

    @Excel(name = "运营部历史备注")
    private String operateHistoryComments;

    @Excel(name = "重分类金额", cellType = Excel.ColumnType.NUMERIC)
    private String reclassAmount;

    @Excel(name = "本月余额-重分类金额", cellType = Excel.ColumnType.NUMERIC)
    private String curBalanceReclassAmount;

    @Excel(name = "重分类科目")
    private String reclassAccountNumber;

    @Excel(name = "财务部初分类")
    private String financialPrimaryClassic;

    @Excel(name = "运营部确认款项性质")
    private String confirmAccountProperty;

    @Excel(name = "非租清单余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal nonLeaseNonClaimAmount;

    @Excel(name = "非租未核销原因")
    private String nonLeaseNonClaimReasons;

    @Excel(name = "核对", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal accountChecking;

    @Excel(name = "非租对账备注")
    private String nonLeaseAccountCheckingComments;

    @Excel(name = "原业务系统网银编号-小网银")
    private String sourceSystemEbankSerialNumber;

    @Excel(name = "银行交易摘要")
    private String bankSummary;

    @Excel(name = "银行交易备注")
    private String bankTradeComments;

    @Excel(name = "付款方账户")
    private String clientAccountsBankNo;

    @Excel(name = "本方账户")
    private String collectionAccountsBankNo;
}
