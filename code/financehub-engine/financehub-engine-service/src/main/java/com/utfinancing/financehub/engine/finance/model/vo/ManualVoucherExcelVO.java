package com.utfinancing.financehub.engine.finance.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherExcelVO</li>
 * <li>CreateTime : 2024/01/03 10:59</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("手工凭证导出VO")
@Data
public class ManualVoucherExcelVO {

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20,comment = "必填，全称")
    private String orgId;

    @ApiModelProperty(value = "记账日期")
    @Excel(name = "记账日期",width = 20,dateFormat = "yyyy-MM-dd",comment = "必填")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    @ApiModelProperty(value = "业务日期")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd",comment = "必填")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "会计期间")
    @Excel(name = "会计期间",width = 20,comment = "必填，格式：YYYYMM")
    private Integer periodCode;

    @ApiModelProperty(value = "凭证类型")
    @Excel(name = "凭证类型",width = 20,comment = "必填，现金/银行/转账/自动银行/自动转账")
    private String voucherType;

    @ApiModelProperty(value = "摘要内容")
    @Excel(name = "摘要内容",width = 20,comment = "必填")
    private String voucherSummary;

    @ApiModelProperty(value = "业务编码")
    @Excel(name = "业务编码",width = 20,comment = "必填,下拉选择")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    @Excel(name = "业务场景名称",width = 20,comment = "必填,下拉选择")
    private String sceneName;

    @ApiModelProperty(value = "细分场景")
    @Excel(name = "细分场景",width = 20,comment = "必填,下拉选择")
    private String subSceneType;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码",width = 20,comment = "必填")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称",width = 20)
    private String accountName;

    @ApiModelProperty(value = "币种编码")
    @Excel(name = "币种",width = 20,comment = "必填，人民币/美元/港元")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    @Excel(name = "汇率",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rate;

    @ApiModelProperty(value = "借方发生额")
    @Excel(name = "借方发生额",width = 20,comment = "必填",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    @Excel(name = "贷方发生额",width = 20,comment = "必填",cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal creditAmount;

//    @ApiModelProperty(value = "是否有现金流量（0：否，1：是）")
//    @Excel(name = "是否有现金流量",width = 20)
//    private String isCashFlow;
//
//    @ApiModelProperty(value = "现金流量标记")
//    @Excel(name = "现金流量标记",width = 20)
//    private String cashFlowMarker;

    @ApiModelProperty(value = "辅助帐摘要")
    @Excel(name = "辅助帐摘要",width = 20)
    private String subsidiaryAccount;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编码",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编码")
    @Excel(name = "客户编码",width = 20)
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "客户类型")
    @Excel(name = "客户类型",width = 20)
    private String clientType;

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loansContractCode;

//    @ApiModelProperty(value = "成本中心")
//    @Excel(name = "成本中心",width = 20)
//    private String costCentre;

//    @ApiModelProperty(value = "&#x5458;&#x5DE5;&#x59D3;&#x540D;")
//    @Excel(name = "员工",width = 20)
//    private String employeeName;

//    @ApiModelProperty(value = "费用类型")
//    @Excel(name = "费用类型",width = 20)
//    private String expenseType;

//    @ApiModelProperty(value = "金融机构")
//    @Excel(name = "金融机构",width = 20)
//    private String financialInstitution;
//
//    @ApiModelProperty(value = "批次号")
//    @Excel(name = "批次号",width = 20)
//    private String batchNum;

    @ApiModelProperty(value = "银行编码")
    @Excel(name = "银行编码",width = 20)
    private String bankNo;

    @ApiModelProperty(value = "物料（合同号）")
//    @Excel(name = "物料（合同号）",width = 20)
    private String materialContractCode;

//    @ApiModelProperty(value = "借据号")
//    @Excel(name = "借据号",width = 20)
//    private String receiptNum;
//
//    @ApiModelProperty(value = "衍生合约编号")
//    @Excel(name = "衍生合约编号",width = 20)
//    private String derivativeContractCode;
//
//    @ApiModelProperty(value = "项目类型（开发项目）")
//    @Excel(name = "项目类型（开发项目）",width = 20)
//    private String projectType;

    @ApiModelProperty(value = "制单人姓名")
    @Excel(name = "制单人姓名",width = 20)
    private String preparerName;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    @Excel(name = "是否涉及其他客户及辅助帐",width = 20)
    private String isRelatedOtherCustomer;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "手工凭证Id")
    private Long manualId;

    @ApiModelProperty(value = "借款合同名称")
    private String loansContractCodeName;

    @ApiModelProperty(value = "成本中心名称")
    private String costCentreName;

    @ApiModelProperty(value = "员工编码")
    private String employeeCode;

    @ApiModelProperty(value = "费用类型名称")
    private String expenseTypeName;

    @ApiModelProperty(value = "金融机构名称")
    private String financialInstitutionName;

    @ApiModelProperty(value = "银行账号名称")
    private String bankNoName;

    @ApiModelProperty(value = "物料（合同名称）")
    private String materialContractName;

    @ApiModelProperty(value = "项目类型名称")
    private String projectTypeName;

    @ApiModelProperty(value = "做为排序索引")
    private Integer index;

    @ApiModelProperty(value = "凭证批次号")
    @Excel(name = "凭证批次号",width = 20,comment = "必填")
    private String batchId;


}
