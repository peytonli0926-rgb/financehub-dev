package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-01
 * @Description : 合同历史表DTO对象
 * @Modified :
 */
@Data
public class ContractHisDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "起租日")
    private LocalDateTime leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "利率浮动类型")
    private String interestRateType;

    @ApiModelProperty(value = "五级分类")
    private String classificationFive;

    @ApiModelProperty(value = "拨备类型")
    private String provisionType;

    @ApiModelProperty(value = "还租方式")
    private String returnType;

    @ApiModelProperty(value = "国产进口")
    private String domesticEntrance;

    @ApiModelProperty(value = "项目区分")
    private String projectDifferentiate;

    @ApiModelProperty(value = "业务板块")
    private String businessPlate;

    @ApiModelProperty(value = "省市")
    private String provinceCity;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "利率")
    private BigDecimal leaseInterestRateYear;

    @ApiModelProperty(value = "财务合同状态更新时间")
    private LocalDateTime financialContractStatusUpdateTime;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "系统标签")
    private String systemLabel;

    @ApiModelProperty(value = "转入公司")
    private String transferOrgId;

    @ApiModelProperty(value = "转入合同号")
    private String transferContractCode;

    @ApiModelProperty(value = "转入合同系统合同状态")
    private String transferContractStatus;

    @ApiModelProperty(value = "特殊标识")
    private String specialFlag;

    @ApiModelProperty(value = "业务类型标签")
    private String businessTypeLabel;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "收益计算")
    private String incomeCalculate;

    @ApiModelProperty(value = "收益计提方式")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "应付设备款")
    private BigDecimal payableDeviceAmount;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivableFirstAmount;

    @ApiModelProperty(value = "出租人保险费")
    private BigDecimal lessorInsuranceAmount;

    @ApiModelProperty(value = "应收承租人履约保证金")
    private BigDecimal receivableMarginAmount;

    @ApiModelProperty(value = "渠道费用 (=应付渠道费用+应付海通渠道费用)")
    private BigDecimal channelFees;

    @ApiModelProperty(value = "应付渠道费用")
    private BigDecimal payableChannelExpense;

    @ApiModelProperty(value = "应付海通渠道费用")
    private BigDecimal payableInnerExpense;

    @ApiModelProperty(value = "应收手续费收入")
    private BigDecimal receivableProcedureAmount;

    @ApiModelProperty(value = "出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)")
    private BigDecimal lessorOtherCosts;

    @ApiModelProperty(value = "应付其他")
    private BigDecimal payableOtherAmount;

    @ApiModelProperty(value = "GPS预估费用")
    private BigDecimal estimateGPSExpense;

    @ApiModelProperty(value = "应付介绍费")
    private BigDecimal payableIntroduce;

    @ApiModelProperty(value = "应付法律费")
    private BigDecimal payableLawAmount;

    @ApiModelProperty(value = "应收厂商返利")
    private BigDecimal receivableFirmRebate;

    @ApiModelProperty(value = "应收保险费")
    private BigDecimal receivableInsuranceAmount;

    @ApiModelProperty(value = "名义留购价")
    private BigDecimal retainedPrice;

    @ApiModelProperty(value = "应收其他")
    private BigDecimal receivableOther;

    @ApiModelProperty(value = "应收服务费")
    private BigDecimal receivableServiceAmount;

    @ApiModelProperty(value = "供应商保证金")
    private BigDecimal vendorMarginAmount;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "合同出单部门")
    private String contractCreateDept;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "提交人")
    private String submitBy;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    private String contractCodeM;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "原组织编码")
    private String oldOrgId;

}
