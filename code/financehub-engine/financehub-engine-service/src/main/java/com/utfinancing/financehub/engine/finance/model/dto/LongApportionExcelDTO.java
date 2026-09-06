package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description : 长期应收款-分摊表DTO对象
 * @Modified :
 */
@Data
public class LongApportionExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "长期应收款编号")
    @Excel(name = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "日期")
    @Excel(name = "日期",dateFormat = "yyyy-MM-dd")
    private Date receivableDate;

    @ApiModelProperty(value = "应收总额")
    @Excel(name = "应收总额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableTotal;

    @ApiModelProperty(value = "应收本金")
    @Excel(name = "应收本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    @Excel(name = "应收利息", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "剩余本金")
    @Excel(name = "剩余本金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal residualPrincipal;

    @ApiModelProperty(value = "摊余成本")
    @Excel(name = "摊余成本", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amortizedCost;

    @ApiModelProperty(value = "确认收入")
    @Excel(name = "确认收入", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal confirmIncome;
    

}
