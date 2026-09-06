package com.utfinancing.financehub.engine.claim.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description : 报销系统-报销单发票明细DTO对象
 * @Modified :
 */
@Data
public class ClaimOrderInvoiceSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "购买机构")
    private String purchaseOrganization;

    @ApiModelProperty(value = "销售方")
    private String seller;

    @ApiModelProperty(value = "开票日期")
    private LocalDateTime invoiceDate;

    @ApiModelProperty(value = "税价合计")
    private String totalAmount;

    @ApiModelProperty(value = "无税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "发票内容")
    private String invoiceContent;

}
