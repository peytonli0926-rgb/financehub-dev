package com.utfinancing.financehub.etl.financial.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimExcelVO</li>
 * <li>CreateTime : 2023/12/11 14:15</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "开票认领导出VO")
@Data
public class InvoiceClaimExcelVO {

    @ApiModelProperty(value = "单据编号")
    @Excel(name = "单据编号",width = 20)
    private String documentNum;

    @ApiModelProperty(value = "业务来源")
    @Excel(name = "业务来源",width = 20)
    private String businessSource;

    @ApiModelProperty(value = "企业编号")
    @Excel(name = "企业编号",width = 20)
    private String enterpriseNum;

    @ApiModelProperty(value = "销方税号")
    @Excel(name = "销方税号",width = 20)
    private String sellerTaxCode;

    @ApiModelProperty(value = "销方名称")
    @Excel(name = "销方名称",width = 20)
    private String sellerName;

    @ApiModelProperty(value = "开票点代码")
    @Excel(name = "开票点代码",width = 20)
    private String invoicePointCode;

    @ApiModelProperty(value = "发票类型")
    @Excel(name = "发票类型",width = 20,readConverterExp = "222=收据,004=专票,007=普票,008=数电专票,009=数电普票")
    private String invoiceType;

    @ApiModelProperty(value = "付款人")
    @Excel(name = "付款人",width = 20)
    private String payer;

    @ApiModelProperty(value = "客户行业")
    @Excel(name = "客户行业",width = 20)
    private String clientIndustry;

    @ApiModelProperty(value = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "单据日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date documentDate;

    @ApiModelProperty(value = "商品编码")
    @Excel(name = "商品编码",width = 20)
    private String productCode;

    @ApiModelProperty(value = "单位")
    @Excel(name = "单位",width = 20)
    private String unit;

    @ApiModelProperty(value = "商品名称")
    @Excel(name = "商品名称",width = 20)
    private String productName;

    @ApiModelProperty(value = "价税合计（含税金额）")
    @Excel(name = "价税合计（含税金额）",width = 20)
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20)
    private String taxRate;

    @ApiModelProperty(value = "金额 (不含税)")
    @Excel(name = "金额 (不含税)",width = 20)
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    @Excel(name = "税额",width = 20)
    private BigDecimal taxValue;

    @ApiModelProperty(value = "购方识别号（税号）")
    @Excel(name = "购方识别号（税号）",width = 20)
    private String purchaserTaxCode;

    @ApiModelProperty(value = "购方名称")
    @Excel(name = "购方名称",width = 20)
    private String purchaserName;

    @ApiModelProperty(value = "购方地址")
    @Excel(name = "购方地址",width = 20)
    private String purchaserDress;

    @ApiModelProperty(value = "购方电话")
    @Excel(name = "购方电话",width = 20)
    private String purchaserTel;

    @ApiModelProperty(value = "购方银行名称")
    @Excel(name = "购方银行名称",width = 20)
    private String purchaserBankName;

    @ApiModelProperty(value = "购方银行账号")
    @Excel(name = "购方银行账号",width = 20)
    private String purchaserBankNum;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注",width = 20)
    private String comments;

    @ApiModelProperty(value = "合同号")
    @Excel(name = "合同号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "合同状态")
    @Excel(name = "合同状态",width = 20)
    private String contractStatus;

    @ApiModelProperty(value = "期数")
    @Excel(name = "期数",width = 20)
    private String periodNum;


    @ApiModelProperty(value = "是否先开开票(0:否，1：是)")
    @Excel(name = "是否先开开票",width = 20,readConverterExp = "0=否,1=是")
    private String isFirstInvoice;

    @ApiModelProperty(value = "上期租金是否全额缴纳(0:否1：是)")
    @Excel(name = "上期租金是否全额缴纳",width = 20,readConverterExp = "0=否,1=是")
    private String isRentPayFull;

    @ApiModelProperty(value = "是否暂不开票（0：否，1：是）")
    @Excel(name = "是否暂不开票",width = 20,readConverterExp = "0=否,1=是")
    private String isHoldInvoice;

    @ApiModelProperty(value = "实收日期/计划还款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实收日期/计划还款日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date planRepayDate;

    @ApiModelProperty(value = "状态")
    @Excel(name = "状态",width = 20,readConverterExp = "0=未认领,1=已认领")
    private String processStatus;

}
