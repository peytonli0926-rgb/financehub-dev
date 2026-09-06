package com.utfinancing.financehub.engine.finance.model.dto;

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
 * @Date : Create in 2023-09-24
 * @Description : 保证金合同余额表单月导出
 * @Modified :
 */
@Data
public class MarginContractBalanceMonthExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "业务日期", cellType = Excel.ColumnType.DATE)
    private String businessDate;

    @ApiModelProperty(value = "财务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "财务日期", cellType = Excel.ColumnType.DATE)
    private String financeDate;

    @ApiModelProperty(value = "组织机构编码")
    @Excel(name = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    @Excel(name = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "保证金类别")
    private String accountName;

    @ApiModelProperty(value = "币种")
    @Excel(name = "币种")
    private String currencyType;

    @ApiModelProperty(value = "合同期末余额")
    @Excel(name = "保证金余额", cellType = Excel.ColumnType.NUMERIC, highLight = true)
    private BigDecimal contractBalance;

    @ApiModelProperty(value = "起租日")
    @Excel(name = "财务起租日", cellType = Excel.ColumnType.DATE)
    private String leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @Excel(name = "约定到期日", cellType = Excel.ColumnType.DATE)
    private String leaseDateEnd;

    @ApiModelProperty(value = "应付一年内到期保证金")
    @Excel(name = "应付一年内到期保证金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal withinOneYearDeposit;

    @ApiModelProperty(value = "保证金利息支出")
    @Excel(name = "保证金利息支出", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depositInterestExpense;

    @ApiModelProperty(value = "保证金利息收入")
    @Excel(name = "保证金利息收入", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depositInterestIncome;

    @ApiModelProperty(value = "是否一年内到期(0:否,1:是)")
    @Excel(name = "是否一年内到期", readConverterExp = "0=否,1=是")
    private String withinOneYear;


    @ApiModelProperty(value = "贷款利率")
    @Excel(name = "贷款利率", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lpr;

    @ApiModelProperty(value = "PV")
    @Excel(name = "PV", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal pv;

    @ApiModelProperty(value = "本金")
    @Excel(name = "本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "本期进入PL")
    @Excel(name = "本期进入PL", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal currentEnterPl;

    @ApiModelProperty(value = "状态(0: 未录入,1: 已录入,2: 已提交,3: 复核通过,4: 复核失败,5: 已传至金蝶)")
    @Excel(name = "状态", readConverterExp = "0=未录入,1=已录入,2=已提交,3=复核通过,4=复核失败,5=已传至金蝶")
    private String marginStatus;


}
