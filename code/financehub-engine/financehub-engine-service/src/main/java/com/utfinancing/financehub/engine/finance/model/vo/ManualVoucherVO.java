package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-03
 * @Description : 手工凭证表VO对象
 * @Modified :
 */
@Data
public class ManualVoucherVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "会计期间（yyyyMM）")
    private Integer periodCode;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期(财务日期)")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "凭证类型")
    private String voucherType;

    @ApiModelProperty(value = "摘要内容")
    private String voucherSummary;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private String rate;

    @ApiModelProperty(value = "借方发生额")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "是否有现金流量（0：否，1：是）")
    private String isCashFlow;

    @ApiModelProperty(value = "现金流量标记")
    private String cashFlowMarker;

    @ApiModelProperty(value = "辅助帐摘要")
    private String subsidiaryAccount;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0：否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "手工Id")
    private Long manualId;

    @ApiModelProperty(value = "借款合同编号")
    private String loansContractCode;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "银行账号")
    private String bankNo;

    @ApiModelProperty(value = "制单人姓名")
    private String preparerName;

    @ApiModelProperty(value = "审批报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "物料（合同号）")
    private String materialContractCode;

    @ApiModelProperty(value = "借据号")
    private String receiptNum;

    @ApiModelProperty(value = "衍生合约编号")
    private String derivativeContractCode;

    @ApiModelProperty(value = "项目类型（开发项目）")
    private String projectType;

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

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    private String isRelatedOtherCustomer;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    //复核人工号
    @ApiModelProperty(value = "复核人工号")
    private String recheckUserNo;

    //复核人姓名
    @ApiModelProperty(value = "复核人姓名")
    private String recheckUserName;

    //创建人姓名
    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    //处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)
    @ApiModelProperty(value = "处理状态")
    private String processStatus;

}
