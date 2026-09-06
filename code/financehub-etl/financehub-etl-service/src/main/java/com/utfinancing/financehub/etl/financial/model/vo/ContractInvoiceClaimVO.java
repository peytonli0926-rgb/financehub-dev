package com.utfinancing.financehub.etl.financial.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.model.vo.ContractInvoiceClaimVO</li>
 * <li>CreateTime : 2023/12/26 16:07</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("合同查询开票认领VO")
@Data
public class ContractInvoiceClaimVO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    @Excel(name = "合同名称",width = 20)
    private String contractName;

    @ApiModelProperty(value = "租赁类型")
    @Excel(name = "租赁类型",width = 20)
    private String LeaseType;

    @ApiModelProperty(value = "开票税率")
    @Excel(name = "开票税率",width = 20)
    private BigDecimal backTaxRate;

    @ApiModelProperty(value = "开票项目")
    @Excel(name = "开票项目",width = 20)
    private String productName;

    @ApiModelProperty(value = "提前开票类型")
    @Excel(name = "提前开票类型",width = 20)
    private String invoiceType;

    @ApiModelProperty(value = "对应期数")
    @Excel(name = "对应期数",width = 20)
    private String periodNum;

    @ApiModelProperty(value = "应开票主体")
    private String orgId;

    @ApiModelProperty(value = "应开票主体名称")
    @Excel(name = "应开票主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "应开票对象")
    @Excel(name = "应开票对象",width = 20)
    private String clientName;

    @ApiModelProperty(value = "应收日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "应收日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date planRepayDate;

    @ApiModelProperty(value = "收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "收款日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date planDate;

    @ApiModelProperty(value = "应收本金")
    @Excel(name = "应收本金",width = 20)
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "应收利息")
    @Excel(name = "应收利息",width = 20)
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "应收租金/其他款项")
    @Excel(name = "应收租金/其他款项",width = 20)
    private BigDecimal rentReceivableAmount;

    @ApiModelProperty(value = "应开票金额")
    @Excel(name = "应开票金额",width = 20)
    private BigDecimal backTaxAmount;

    @ApiModelProperty(value = "应开票税额")
    @Excel(name = "应开票税额",width = 20)
    private BigDecimal backTaxValue;

    @ApiModelProperty(value = "实际开票主体")
    @Excel(name = "实际开票主体",width = 20)
    private String sellerTaxCode;

    @ApiModelProperty(value = "实际开票对象")
    @Excel(name = "实际开票对象",width = 20)
    private String payer;

    @ApiModelProperty(value = "实际开票金额")
    @Excel(name = "实际开票金额",width = 20)
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "实际开票税率")
    @Excel(name = "实际开票税率",width = 20)
    private String taxRate;

    @ApiModelProperty(value = "实际开票税额")
    @Excel(name = "实际开票税额",width = 20)
    private BigDecimal taxValue;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "开票日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date documentDate;

    @ApiModelProperty(value = "发票号码")
    @Excel(name = "发票号码",width = 20)
    private String invoiceNumber;

    @ApiModelProperty(value = "异常类型")
    @Excel(name = "异常类型",width = 20)
    private String exceptionType;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注",width = 20)
    private String comments;

    @ApiModelProperty(value = "系统来源")
    private String systemCode;

}
