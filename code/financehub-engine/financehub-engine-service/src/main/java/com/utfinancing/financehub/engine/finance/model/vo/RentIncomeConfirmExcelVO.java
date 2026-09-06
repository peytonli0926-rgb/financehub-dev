package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description : 租金收入确认VO excel对象
 * @Modified :
 */
@Data
public class RentIncomeConfirmExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "记账月份")
    @Excel(name = "记账月份", dateFormat = "yyyy-MM")
    private Date accountMonth;

    @ApiModelProperty(value = "当月应收租金")
    @Excel(name = "当月应收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthReceivableRent;

    @ApiModelProperty(value = "当月计提税金")
    @Excel(name = "当月计提税金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthTax;

    @ApiModelProperty(value = "确认收入金额")
    @Excel(name = "确认收入金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthRentIncome;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;


}
