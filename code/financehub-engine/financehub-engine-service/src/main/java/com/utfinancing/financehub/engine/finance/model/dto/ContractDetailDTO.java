package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description : 合同详情DTO,包含数仓的合同基本信息
 * @Modified :
 */
@Data
public class ContractDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    //以下为合同信息
    @ApiModelProperty(value = "合同编号", position = 1)
    private String contractCode;

    @ApiModelProperty(value = "合同名称", position = 2)
    private String contractName;

    @ApiModelProperty(value = "签约主体", position = 3)
    private String orgId;

    @ApiModelProperty(value = "业务合同状态", position = 4)
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态", position = 5)
    private String financialContractStatus;

    @ApiModelProperty(value = "合同出单部门", position = 6)
    private String contractCreateDept;

    @ApiModelProperty(value = "货币类型", position = 7)
    private String currencyType;

    @ApiModelProperty(value = "所属系统", position = 8)
    private String systemCode;

    @ApiModelProperty(value = "项目编号", position = 9)
    private String projectNumber;

    @ApiModelProperty(value = "客户编号", position = 10)
    private String clientCode;

    @ApiModelProperty(value = "客户名称", position = 11)
    private String clientName;

    @ApiModelProperty(value = "客户类型", position = 12)
    private String clientType;

    @ApiModelProperty(value = "服务费合同（主合同编号）", position = 12)
    private String contractCodeM;

    @ApiModelProperty(value = "组织机构代码/身份证号", position = 13)
    private String clientIdNumber;

    @ApiModelProperty(value = "起租日", position = 14)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime leaseDateStart;

    @ApiModelProperty(value = "到期日", position = 15)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "签约日期", position = 16)
    private String dwContractSignDate;

    //以下为业务信息
    @ApiModelProperty(value = "项目立项日期", position = 17)
    private LocalDateTime projectApproveDate;

    @ApiModelProperty(value = "业务类型编码", position = 18)
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称（租赁大类）", position = 19)
    private String businessName;

    @ApiModelProperty(value = "租赁类型", position = 20)
    private String leaseType;

    @ApiModelProperty(value = "租赁设备类型", position = 21)
    private String dwLeaseDeviceType;

    @ApiModelProperty(value = "业务种类(业务子类)", position = 22)
    private String dwBusinessType;

    @ApiModelProperty(value = "车辆类型", position = 23)
    private String vehicleType;

    @ApiModelProperty(value = "国标行业([内部标签]行业分类标签)", position = 24)
    private String dwIndustryTag;

    @ApiModelProperty(value = "新行业分类", position = 25)
    private String newIndustry;

    @ApiModelProperty(value = "海通一大一小标签（[内部标签]客户大小标签）", position = 26)
    private String dwLargeSmallTag;

    @ApiModelProperty(value = "海通客户性质标签([内部标签]内部客户性质)", position = 27)
    private String dwClientNature;

    @ApiModelProperty(value = "办事处", position = 28)
    private String office;

    @ApiModelProperty(value = "厂商", position = 29)
    private String manufacturer;

    @ApiModelProperty(value = "区域", position = 30)
    private String dwDistrict;

    @ApiModelProperty(value = "省", position = 31)
    private String province;

    @ApiModelProperty(value = "市", position = 32)
    private String city;

    @ApiModelProperty(value = "业绩归属部门", position = 33)
    private String performanceBelongDept;

    @ApiModelProperty(value = "业绩归属员工", position = 34)
    private String performanceBelongEmployee;

    @ApiModelProperty(value = "业务板块", position = 35)
    private String businessPlate;

    @ApiModelProperty(value = "发票类型", position = 36)
    private String invoiceType;

    @ApiModelProperty(value = "开票标识", position = 37)
    private String invoicingFlag;

    @ApiModelProperty(value = "利率浮动类型", position = 38)
    private String interestRateType;

    @ApiModelProperty(value = "收益计算", position = 39)
    private String incomeCalculate;

    @ApiModelProperty(value = "收益计提方式", position = 40)
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "设备金额", position = 41)
    private BigDecimal deviceAmount;

    @ApiModelProperty(value = "税率", position = 42)
    private BigDecimal taxRate;

    @ApiModelProperty(value = "拟出表", position = 43)
    private String dwPlanTable;

    @ApiModelProperty(value = "是否出表", position = 44)
    private String dwHasTable;

    @ApiModelProperty(value = "出表类型", position = 45)
    private String tableType;

    @ApiModelProperty(value = "ABS出表项目名称", position = 46)
    private String absTableProjectName;

    @ApiModelProperty(value = "封包日", position = 37)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime closePackageDate;

    @ApiModelProperty(value = "发行日", position = 48)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime releaseDate;

    @ApiModelProperty(value = "受让方", position = 49)
    private String receiver;

    @ApiModelProperty(value = "转让时间", position = 50)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime transferDate;

    @ApiModelProperty(value = "是否内部转让合同", position = 51)
    private String transferFlag;

    @ApiModelProperty(value = "转让前合同号", position = 52)
    private String beforeContractCode;

    @ApiModelProperty(value = "转让前主体", position = 53)
    private String beforeSubject;

    @ApiModelProperty(value = "评估主体", position = 54)
    private String assessmentSubject;

    @ApiModelProperty(value = "业务员", position = 55)
    private String businessMan;

    @ApiModelProperty(value = "逾期阶段", position = 56)
    private String overdueStage;

    @ApiModelProperty(value = "五级分类", position = 57)
    private String classificationFive;

    @ApiModelProperty(value = "风险阶段划分", position = 58)
    private String riskStage;


    @ApiModelProperty(value = "设备价格",position = 59)
    private BigDecimal payableDeviceAmount;

    @ApiModelProperty(value = "租金首付款",position = 60)
    private BigDecimal receivableFirstAmount;

    @ApiModelProperty(value = "出租人保险费",position = 61)
    private BigDecimal lessorInsuranceAmount;

    @ApiModelProperty(value = "应收承租人履约保证金",position = 62)
    private BigDecimal receivableMarginAmount;
    // 渠道费用 (=应付渠道费用+应付海通渠道费用)
    @ApiModelProperty(value = "渠道费用",position = 63)
    private BigDecimal channelFees;

    // 应收手续费收入
    @ApiModelProperty(value = "应收手续费收入",position = 64)
    private BigDecimal receivableProcedureAmount;

    // 出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
    @ApiModelProperty(value = "出租人其它成本",position = 65)
    private BigDecimal lessorOtherCosts;

    // 名义留购价
    @ApiModelProperty(value = "名义留购价",position = 66)
    private BigDecimal retainedPrice;

    // 应收保险费
    @ApiModelProperty(value = "承租人保险费",position = 67)
    private BigDecimal receivableInsuranceAmount;

    // 应收其他
    @ApiModelProperty(value = "其他收入（含增值税）",position = 68)
    private BigDecimal receivableOther;

    // 供应商保证金
    @ApiModelProperty(value = "供应商履约保证金",position = 69)
    private BigDecimal vendorMarginAmount;

    // 应收服务费
    @ApiModelProperty(value = "咨询服务收入（含增值税）",position = 70)
    private BigDecimal receivableServiceAmount;

    // 租赁合同总计 = 租金首付款+应收手续费收入+应收租金总额？
    @ApiModelProperty(value = "租赁合同总计",position = 71)
    private BigDecimal rentContractTotal;


    // 设备价格-租金首付款
    @ApiModelProperty(value = "租金概算本金",position = 72)
    private BigDecimal rentPrincipal;

    // 租赁合同收入总计 = 租赁合同总计+承租人履约保证金+厂商返利（含增值税）+名义留购价+期末残值+供应商履约保证金+承租人保险费+其他收入（含增值税）
    @ApiModelProperty(value = "租赁合同收入总计",position = 73)
    private BigDecimal rentContractIncomesTotal;


    // 净融资额=设备价格+出租人保险费+渠道费用+出租人其他成本-（租金首付款+承租人履约保证金+手续费收入（含增值税）+厂商返利（含增值税）+供应商履约保证金+其他收入+咨询服务收入+起租前已收租金？）
    @ApiModelProperty(value = "净融资额",position = 74)
    private BigDecimal financingAmount;

    // 租赁销售额=设备价格+出租人保险费+渠道费用+出租人其他成本-（租金首付款+承租人履约保证金+手续费收入（含增值税）+厂商返利（含增值税）+供应商履约保证金+其他收入+咨询服务收入+起租前已收租金？）
    @ApiModelProperty(value = "租赁销售额",position = 75)
    private BigDecimal rentSalesAmount;

    @ApiModelProperty(value = "厂商返利",position = 76)
    private BigDecimal receivableFirmRebate;
    
    //end region交易结构

    //应收租金总额=eg_repayment_plan 表按照合同取rent_amount值

    // 应付渠道费用
    @ApiModelProperty(value = "应付渠道费用",position = 77)
    private BigDecimal payableChannelExpense;
    // 应付海通渠道费用
    @ApiModelProperty(value = "应付海通渠道费用",position = 78)
    private BigDecimal payableInnerExpense;

    @ApiModelProperty(value = "应付其他",position = 79)
    // 应付其他
    private BigDecimal payableOtherAmount;
    // GPS预估费用
    @ApiModelProperty(value = "GPS预估费用",position = 80)
    private BigDecimal estimateGPSExpense;
    // 应付介绍费
    @ApiModelProperty(value = "应付介绍费",position = 81)
    private BigDecimal payableIntroduce;
    // 应付法律费
    @ApiModelProperty(value = "应付法律费",position = 82)
    private BigDecimal payableLawAmount;

    //租金概算本金 = 【设备价格payable_device_amount】-【租金首付款receivable_first_amount】
    @ApiModelProperty(value = "租金概算本金",position = 82)
    private BigDecimal rentEstimatePrincipal;

    //还款标识 期初(下还),期末(上还)
    @ApiModelProperty(value = "还款标识",position = 83)
    private String payMethod;

    //还租方式
    @ApiModelProperty(value = "还款节奏",position = 84)
    private String returnType;

    @ApiModelProperty(value = "服务合同主体名称", position = 12)
    private String mainOrgIdName;


    @ApiModelProperty(value = "期末残值", position = 13)
    private BigDecimal lastCost;
}
