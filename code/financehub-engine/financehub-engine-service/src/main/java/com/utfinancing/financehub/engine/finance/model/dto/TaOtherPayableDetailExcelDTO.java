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
 * @Date : Create in 2024-05-22
 * @Description : ta其他应付款明细DTO对象
 * @Modified :
 */
@Data
public class TaOtherPayableDetailExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "重分类月份")
    @Excel(name = "重分类月份",dateFormat = "yyyy-MM")
    private Date reclassificationMonth;

    @ApiModelProperty(value = "网银到账主体")
    @Excel(name = "网银到账主体")
    private String bankOrgId;

    @ApiModelProperty(value = "业务系统网银编号-小网银")
    @Excel(name = "业务系统网银编号-小网银")
    private String ebankBatchNo;

    @ApiModelProperty(value = "财务初分类")
    @Excel(name = "财务初分类")
    private String financialPrimaryClassic;

    @ApiModelProperty(value = "运营部确认款项性质")
    @Excel(name = "运营部确认款项性质")
    private String confirmAccountProperty;

    @ApiModelProperty(value = "重分类金额")
    @Excel(name = "重分类金额",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal reclassificationAmount;

    @ApiModelProperty(value = "重分类科目编码")
    @Excel(name = "重分类科目编码")
    private String accountCode;

}
