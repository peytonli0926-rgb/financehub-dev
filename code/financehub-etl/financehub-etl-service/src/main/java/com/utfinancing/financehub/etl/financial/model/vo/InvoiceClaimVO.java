package com.utfinancing.financehub.etl.financial.model.vo;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-10
 * @Description : VO对象
 * @Modified :
 */
@Data
public class InvoiceClaimVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "单据编号")
    private String documentNum;

    @ApiModelProperty(value = "企业编号")
    private String enterpriseNum;

    @ApiModelProperty(value = "销方税号")
    private String sellerTaxCode;

    @ApiModelProperty(value = "开票点代码")
    private String invoicePointCode;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "付款人")
    private String payer;

    @ApiModelProperty(value = "客户行业")
    private String clientIndustry;

    @ApiModelProperty(value = "单据日期")
    private LocalDateTime documentDate;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "价税合计（含税金额）")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "金额 (不含税)")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxValue;

    @ApiModelProperty(value = "是否先开开票(0:否，1：是)")
    private String isFirstInvoice;

    @ApiModelProperty(value = "上期租金是否全额缴纳(0:否1：是)")
    private String isRentPayFull;

    @ApiModelProperty(value = "是否暂不开票（0：否，1：是）")
    private String isHoldInvoice;

    @ApiModelProperty(value = "购方识别号（税号）")
    private String purchaserTaxCode;

    @ApiModelProperty(value = "购方名称")
    private String purchaserName;

    @ApiModelProperty(value = "购方地址")
    private String purchaserDress;

    @ApiModelProperty(value = "购方电话")
    private String purchaserTel;

    @ApiModelProperty(value = "购方银行名称")
    private String purchaserBankName;

    @ApiModelProperty(value = "购方银行账号")
    private String purchaserBankNum;

    @ApiModelProperty(value = "实收日期/计划还款日期")
    private LocalDateTime planRepayDate;

    @ApiModelProperty(value = "期数")
    private String periodNum;

    @ApiModelProperty(value = "商品编码")
    private String productCode;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数量")
    private String quantity;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0：否1：是）")
    private String delFlag;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "数据来源（0：纸质发票，1：电子发票）")
    private String sourceFrom;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "是否自动生成（0：否，1：是）")
    private String isAutoGeneration;

    @ApiModelProperty(value = "大类")
    private String mainCategory;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "main_category映射大类")
    private String mappingCategory;

    @ApiModelProperty(value = "同步系统发票主键id")
    private Long systemInvoiceId;

    @ApiModelProperty(value = "销方名称")
    private String sellerName;

    @ApiModelProperty(value = "业务来源")
    private String businessSource;

    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "认领状态，0-未认领，1-已认领")
    private String claimStatus;

    @ApiModelProperty(value = "Back表含税金额")
    private BigDecimal backTaxAmount;

    @ApiModelProperty(value = "Back表含税税额")
    private BigDecimal backTaxValue;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "生成凭证报错原因")
    private String errorMessage;

    @ApiModelProperty("签约主体")
    private String orgId;

    @ApiModelProperty("处理状态,0:未认领，1：已认领")
    private String processStatus;

    @ApiModelProperty("手工表id")
    private Long manualId;

}
