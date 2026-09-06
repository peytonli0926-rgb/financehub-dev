package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description : 出租登记-租金计划VO excel对象
 * @Modified :
 */
@Data
public class RentRegisterDetailExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "房产租赁合同编号")
    private String contractCode;

    @ApiModelProperty(value = "应付日期")
    @Excel(name = "应付日期", dateFormat = "yyyy-MM-dd", cellType = Excel.ColumnType.DATE)
    private Date planDate;

    @ApiModelProperty(value = "所属期")
    @Excel(name = "所属期")
    private String period;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "当月应收租金")
    @Excel(name = "当月应收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthReceivableRent;

    @ApiModelProperty(value = "当月计提税金")
    @Excel(name = "当月计提税金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthTax;

    @ApiModelProperty(value = "当月租金收入")
    @Excel(name = "当月租金收入", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthRentIncome;

    @ApiModelProperty(value = "实际回笼日期")
    @Excel(name = "实际回笼日期", dateFormat = "yyyy-MM-dd", cellType = Excel.ColumnType.DATE)
    private Date actualRepaymentDate;

    @ApiModelProperty(value = "回笼金额")
    @Excel(name = "回笼金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal repaymentAmount;

    @ApiModelProperty(value = "网银编号")
    @Excel(name = "网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "开票日期")
    @Excel(name = "开票日期", dateFormat = "yyyy-MM-dd", cellType = Excel.ColumnType.DATE)
    private Date invoiceDate;

    @ApiModelProperty(value = "开票金额")
    @Excel(name = "开票金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal invoiceAmount;

}
