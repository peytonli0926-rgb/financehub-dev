package com.utfinancing.financehub.engine.claim.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description :   ClaimOrderInvoice查询from对象
 * @Modified :
 */
@ApiModel("ClaimOrderInvoice查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClaimOrderInvoiceQueryDTO extends BaseQueryDTO{

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
