package com.utfinancing.financehub.engine.finance.model.dto;
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
@Data
public class ReceiveTaxDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "单据状态")
    private String billStatus;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "开票日期")
    private Date invoiceDate;

    @ApiModelProperty(value = "购方名称")
    private String purchaserName;

    @ApiModelProperty(value = "购方税号")
    private String purchaserTaxCode;

    @ApiModelProperty(value = "购方地址")
    private String purchaserDressTel;

    @ApiModelProperty(value = "购方银行账号")
    private String purchaserBankNameNum;

    @ApiModelProperty(value = "销方名称")
    private String sellerName;

    @ApiModelProperty(value = "销方税号")
    private String sellerTaxCode;

    @ApiModelProperty(value = "销方地址电话")
    private String sellerDressTel;

    @ApiModelProperty(value = "销方银行账号")
    private String sellerBankNameNum;

    @ApiModelProperty(value = "备注（合同编号）")
    private String remark;

    @ApiModelProperty(value = "收款人")
    private String receiptName;

    @ApiModelProperty(value = "复核人")
    private String reviewName;

    @ApiModelProperty(value = "开票人")
    private String drawerName;

    @ApiModelProperty(value = "开票机编号")
    private String invoiceMachineNo;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "规格型号")
    private String model;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数量")
    private String quantity;

    @ApiModelProperty(value = "金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税收分类编码")
    private String taxClassificationCode;

    @ApiModelProperty(value = "税收分类编码名称")
    private String taxClassificationName;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
