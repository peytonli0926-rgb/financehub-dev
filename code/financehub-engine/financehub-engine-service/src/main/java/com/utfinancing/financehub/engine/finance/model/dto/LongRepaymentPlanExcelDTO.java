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
 * @Description : 长期应收款-偿还计划DTO对象
 * @Modified :
 */
@Data
public class LongRepaymentPlanExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "长期应收款编号")
    @Excel(name = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "应收日期")
    @Excel(name = "应收日期",dateFormat = "yyyy-MM-dd")
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


}
