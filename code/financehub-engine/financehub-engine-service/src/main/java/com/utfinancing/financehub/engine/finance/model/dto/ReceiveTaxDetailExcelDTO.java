package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : 应交销项税明细DTO对象
 * @Modified :
 */
@ApiModel(value = "应交销项税导入DTO")
@Data
public class ReceiveTaxDetailExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "单据号")
    private String billNo;

    @Excel(name = "单据状态")
    private String billStatus;

    @Excel(name = "发票类型")
    private String invoiceType;

    @Excel(name = "发票代码")
    private String invoiceCode;

    @Excel(name = "发票号码")
    private String invoiceNumber;

    @Excel(name = "开票日期(yyyy-MM-dd)",dateFormat = "yyyy-MM-dd")
    private Date invoiceDate;

    @Excel(name = "购方名称")
    private String purchaserName;

    @Excel(name = "购方税号")
    private String purchaserTaxCode;

    @Excel(name = "购方地址")
    private String purchaserDressTel;

    @Excel(name = "购方银行账号")
    private String purchaserBankNameNum;

    @Excel(name = "销方名称")
    private String sellerName;

    @Excel(name = "销方税号")
    private String sellerTaxCode;

    @Excel(name = "销方地址电话")
    private String sellerDressTel;

    @Excel(name = "销方银行账号")
    private String sellerBankNameNum;

    @Excel(name = "备注（合同编号）")
    private String remark;

    @Excel(name = "收款人")
    private String receiptName;

    @Excel(name = "复核人")
    private String reviewName;

    @Excel(name = "开票人")
    private String drawerName;

    @Excel(name = "开票机编号")
    private String invoiceMachineNo;

    @Excel(name = "商品名称")
    private String productName;

    @Excel(name = "规格型号")
    private String model;

    @Excel(name = "单位")
    private String unit;

    @Excel(name = "数量")
    private String quantity;

    @Excel(name = "金额",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal totalAmount;

    @Excel(name = "税率",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxRate;

    @Excel(name = "税额",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxAmount;

    @Excel(name = "税收分类编码")
    private String taxClassificationCode;

    @Excel(name = "税收分类编码名称")
    private String taxClassificationName;

}
