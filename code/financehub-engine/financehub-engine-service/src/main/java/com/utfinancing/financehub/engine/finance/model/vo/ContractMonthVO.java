package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2025-04-21
 * @Description : 合同月表VO对象
 * @Modified :
 */
@Data
public class ContractMonthVO implements Serializable{
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
    private String contractAmount;

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

    @ApiModelProperty(value = "利率")
    private String leaseInterestRateYear;

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
    private String taxRate;

    @ApiModelProperty(value = "收益计算")
    private String incomeCalculate;

    @ApiModelProperty(value = "收益计提方式")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "应付设备款")
    private String payableDeviceAmount;

    @ApiModelProperty(value = "应收首付款")
    private String receivableFirstAmount;

    @ApiModelProperty(value = "出租人保险费")
    private String lessorInsuranceAmount;

    @ApiModelProperty(value = "应收承租人履约保证金")
    private String receivableMarginAmount;

    @ApiModelProperty(value = "渠道费用 (=应付渠道费用+应付海通渠道费用)")
    private String channelFees;

    @ApiModelProperty(value = "应付渠道费用")
    private String payableChannelExpense;

    @ApiModelProperty(value = "应付海通渠道费用")
    private String payableInnerExpense;

    @ApiModelProperty(value = "应收手续费收入")
    private String receivableProcedureAmount;

    @ApiModelProperty(value = "出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)")
    private String lessorOtherCosts;

    @ApiModelProperty(value = "应付其他")
    private String payableOtherAmount;

    @ApiModelProperty(value = "GPS预估费用")
    private String estimateGPSExpense;

    @ApiModelProperty(value = "应付介绍费")
    private String payableIntroduce;

    @ApiModelProperty(value = "应付法律费")
    private String payableLawAmount;

    @ApiModelProperty(value = "应收厂商返利")
    private String receivableFirmRebate;

    @ApiModelProperty(value = "应收保险费")
    private String receivableInsuranceAmount;

    @ApiModelProperty(value = "名义留购价")
    private String retainedPrice;

    @ApiModelProperty(value = "应收其他")
    private String receivableOther;

    @ApiModelProperty(value = "应收服务费")
    private String receivableServiceAmount;

    @ApiModelProperty(value = "供应商保证金")
    private String vendorMarginAmount;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "合同出单部门")
    private String contractCreateDept;

    @ApiModelProperty(value = "应交销项税余额")
    private String outtaxBalance;

    @ApiModelProperty(value = "租赁收益余额")
    private String leaseRevenueBalance;

    @ApiModelProperty(value = "转回拨备（减值准备余额）")
    private String depreciationLossBalance;

    @ApiModelProperty(value = "还款标识")
    private String payMethod;

    @ApiModelProperty(value = "主合同编号")
    private String contractCodeM;

    @ApiModelProperty(value = "应付手续费金额")
    private String payableProcedureCost;

    @ApiModelProperty(value = "邮储项目类型")
    private String postalSavingsProjectType;

    @ApiModelProperty(value = "归集财务合同状态")
    private String financialContractStatusImputation;

    @ApiModelProperty(value = "gps费用单价")
    private String gpsUnitPrice;

    @ApiModelProperty(value = "应付经销商服务费")
    private String payableService;

    @ApiModelProperty(value = "收车费汇总总额")
    private String payableRecycleCarAmount;

    @ApiModelProperty(value = "手环成本")
    private String payableBraceletCost;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "0:非手工起租 1:手工起租")
    private String manualLeaseFlag;

    @ApiModelProperty(value = "合同业务大类")
    private String businessCategory;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "租赁大表报表查询时是否过滤标志 0:过滤, 1:不过滤")
    private String reportFlag;

    @ApiModelProperty(value = "是否抵债资产，0否，1是")
    private String isDzzc;

    @ApiModelProperty(value = "租赁合同总计")
    private String rentContractTotal;

    @ApiModelProperty(value = "期末残值")
    private String lastCost;

    @ApiModelProperty(value = "起租前已收租金")
    private String leaseBeforeReceviedAmount;

    @ApiModelProperty(value = "核销合同开票时转回的拨备发生额")
    private String depreciationReservesAmountKp;

    @ApiModelProperty(value = "核销合同收款时转回的拨备发生额")
    private String depreciationReservesAmountSk;

    @ApiModelProperty(value = "核销合同税金计提金额")
    private String outtaxAmount;

    @ApiModelProperty(value = "核销合同收益计提金额")
    private String leaseRevenueAmount;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "实收服务费")
    private String actualServiceAmount;

    @ApiModelProperty(value = "是否做过偿还计划变更(0:否, 1:是)")
    private String isChangeRepayment;

    @ApiModelProperty(value = "实收手续费(含税)")
    private String paidHandlingFees;

    @ApiModelProperty(value = "其它收入(含税)")
    private String otherIncome;

    @ApiModelProperty(value = "应收租金")
    private String receivableRent;

    @ApiModelProperty(value = "应开票/计提税额")
    private String accrualAmount;

    @ApiModelProperty(value = "税金计提金额")
    private String taxAmount;

}
