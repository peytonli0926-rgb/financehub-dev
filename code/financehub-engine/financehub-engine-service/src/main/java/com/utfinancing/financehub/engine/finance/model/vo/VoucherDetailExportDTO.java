package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VoucherDetailExportDTO {

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体",width = 20)
    private String orgName;

    @ApiModelProperty(value = "会计期间")
    @Excel(name = "会计期间",width = 20)
    private Integer periodCode;

    @ApiModelProperty(value = "业务日期")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDate businessDate;

    @ApiModelProperty(value = "记账日期")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd",cellType = Excel.ColumnType.DATE)
    private LocalDate voucherDate;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "凭证类型;refDict")
    @Excel(name = "凭证类型",width = 20)
    private String voucherName;

    @ApiModelProperty(value = "凭证号")
    @Excel(name = "凭证号",width = 20)
    private Long voucherNum;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    @Excel(name = "业务场景",width = 20)
    private String sceneName;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "细分场景")
    @Excel(name = "细分场景",width = 20)
    private String subSceneTypeName;


    @ApiModelProperty(value = "凭证头摘要")
    @Excel(name = "凭证头摘要",width = 20)
    private String voucherSummary;

    @ApiModelProperty(value = "科目代码")
    @Excel(name = "科目代码",width = 20)
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称",width = 20)
    private String accountName;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "币种")
    @Excel(name = "币种",width = 20)
    private String currenctName;

    @ApiModelProperty(value = "汇率")
    @Excel(name = "汇率",width = 20)
    private String taxRate;

    @ApiModelProperty(value = "借方发生额")
    @Excel(name = "借方发生额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    @Excel(name = "贷方发生额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal creditAmount;


    @ApiModelProperty(value = "凭证行摘要")
    @Excel(name = "凭证行摘要",width = 20)
    private String voucherEntrySummary;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;


    //借款合同编号
    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String billContractCode;

//    @ApiModelProperty(value = "成本中心")
//    @Excel(name = "成本中心",width = 20)
//    private String costCentre;

    @ApiModelProperty(value = "银行账号")
    @Excel(name = "银行账号",width = 20)
    private String bankAccount;

    @ApiModelProperty(value = "制单人工号")
    private String createUserNo;

    @ApiModelProperty(value = "制单人姓名")
    @Excel(name = "制单人",width = 20)
    private String createUserName;

    @ApiModelProperty(value = "复核人工号")
    private String recheckUserNo;

    @ApiModelProperty(value = "复核人姓名")
    @Excel(name = "复核人",width = 20)
    private String recheckUserName;


    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态",width = 20)
    private String voucherStatus;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    @Excel(name = "是否涉及其他客户及辅助帐",width = 20,readConverterExp = "0=否,1=是")
    private String isRelatedOtherCustomer;

}
