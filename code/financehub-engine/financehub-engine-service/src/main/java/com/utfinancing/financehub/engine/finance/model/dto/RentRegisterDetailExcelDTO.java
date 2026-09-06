package com.utfinancing.financehub.engine.finance.model.dto;

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
 * @Description : 出租登记-租金计划DTO excel对象
 * @Modified :
 */
@Data
public class RentRegisterDetailExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房产租赁合同编号")
    @Excel(name = "房产租赁合同编号")
    private String contractCode;

    @ApiModelProperty(value = "应付日期")
    @Excel(name = "应付日期",cellType = Excel.ColumnType.DATE,dateFormat = "yyyy-MM-dd")
    private Date planDate;

    @ApiModelProperty(value = "所属期")
    @Excel(name = "所属期")
    private String period;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

}
