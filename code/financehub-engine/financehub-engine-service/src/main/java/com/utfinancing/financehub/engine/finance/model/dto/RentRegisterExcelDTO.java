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
 * @Date : Create in 2024-04-07
 * @Description : 出租登记DTO对象
 * @Modified :
 */
@Data
public class RentRegisterExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房产租赁合同编号")
    @Excel(name = "房产租赁合同编号")
    private String contractCode;

    @ApiModelProperty(value = "资产编号")
    @Excel(name = "资产编号（存在多个用英文逗号隔开，例如A,B）")
    private String assetNumber;

    @ApiModelProperty(value = "转出时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "转出时间", cellType = Excel.ColumnType.DATE)
    private Date transferOutDate;

    @ApiModelProperty(value = "客户编号")
    @Excel(name = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "起租日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "起租日",  cellType = Excel.ColumnType.DATE)
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "到期日",  cellType = Excel.ColumnType.DATE)
    private Date leaseDateEnd;

    @ApiModelProperty(value = "租赁保证金")
    @Excel(name = "租赁保证金",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentBond;

}
