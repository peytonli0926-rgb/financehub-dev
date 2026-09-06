package com.utfinancing.financehub.engine.finance.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 合同实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract")
public class ContractEntityUpdateVo extends Model<ContractEntityUpdateVo> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //组织机编码
    private String orgId;

    //利率
    private BigDecimal leaseInterestRateYear;

    //客户类型
    private String clientType;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //业务类型编码
    private String businessCode;

    //业务类型名称
    private String businessName;

    //发票类型
    private String invoiceType;

    //行业
    private String industry;

    //租赁类型
    private String leaseType;

    //合同状态
    private String contractStatus;

    //利率浮动类型
    private String interestRateType;

    //五级分类
    private String classificationFive;

    //拨备类型
    private String provisionType;

    //还租方式
    private String returnType;

    //国产进口
    private String domesticEntrance;

    //项目区分
    private String projectDifferentiate;

    //业务板块
    private String businessPlate;

    //省市
    private String provinceCity;

    //币种
    private String currencyType;

    //合同金额
    private BigDecimal contractAmount;

    //业务类型标签
    private String businessTypeLabel;

    //税率
    private BigDecimal taxRate;

    //来源系统
    private String systemCode;

    //合同出单部门
    private String contractCreateDept;

    //region交易结构
    // 应付设备款
    private BigDecimal payableDeviceAmount;
    // 应收首付款
    private BigDecimal receivableFirstAmount;
    // 出租人保险费
    private BigDecimal lessorInsuranceAmount;
    // 应收承租人履约保证金
    private BigDecimal receivableMarginAmount;
    // 渠道费用 (=应付渠道费用+应付海通渠道费用)
    private BigDecimal channelFees;
    // 应付渠道费用
    private BigDecimal payableChannelExpense;
    // 应付海通渠道费用
    private BigDecimal payableInnerExpense;
    // 应收手续费收入
    private BigDecimal receivableProcedureAmount;
    // 出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
    private BigDecimal lessorOtherCosts;
    // 手环成本
    private BigDecimal payableBraceletCost;
    // 应付其他
    private BigDecimal payableOtherAmount;
    // GPS预估费用
    private BigDecimal estimateGPSExpense;
    // 应付介绍费
    private BigDecimal payableIntroduce;
    // 应付法律费
    private BigDecimal payableLawAmount;
    // 应收厂商返利
    private BigDecimal receivableFirmRebate;
    // 应收保险费
    private BigDecimal receivableInsuranceAmount;
    // 名义留购价
    private BigDecimal retainedPrice;
    // 应收其他
    private BigDecimal receivableOther;
    // 应收服务费
    private BigDecimal receivableServiceAmount;
    // 供应商保证金
    private BigDecimal vendorMarginAmount;
    //end region交易结构


    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;

    //特殊标识
    private String specialFlag;

    //财务合同状态更新时间
    private Date financialContractStatusUpdateTime;

    //财务合同状态
    private String financialContractStatus;

    //转入公司
    private String transferOrgId;

    //转入合同号
    private String transferContractCode;

    //转入合同系统合同状态
    private String transferContractStatus;

    //收益计算
    private String incomeCalculate;
    //收益计提方式
    private String incomeProvisionMethod;
    //开票标识
    private String invoicingFlag;

    //应交销项税余额
    private BigDecimal outtaxBalance;

    //租赁收益余额
    private BigDecimal leaseRevenueBalance;

    //转回拨备（减值准备余额）
    private BigDecimal depreciationLossBalance;

    //还款标识 期初(下还),期末(上还)
    private String payMethod;

    //主合同编号
    private String contractCodeM;

    //应付手续费金额
    private BigDecimal payableProcedureCost;

    //邮储项目类型
    private String postalSavingsProjectType;

    //归集财务合同状态
    private String financialContractStatusImputation;

    //gps费用单价
    private BigDecimal gpsUnitPrice;

    //应付经销商服务费
    private BigDecimal payableService;

    //收车费汇总金额
    private BigDecimal payableRecycleCarAmount;

    //业务日期
    private LocalDate businessDate;

    // 0:非手工起租 1:手工起租
    private String manualLeaseFlag;

    // 业务合同大类
    private String businessCategory;

    //省
    @ApiModelProperty(value = "省")
    private String province;

    //市
    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "是否抵债资产，0否，1是")
    private String isDzzc;
    // 租赁合同总计
    private BigDecimal rentContractTotal;
    // 期末残值
    private BigDecimal lastCost;
    // 起租前已收租金
    private BigDecimal leaseBeforeReceviedAmount;

    @ApiModelProperty(value = "核销合同开票时转回的拨备发生额")
    private BigDecimal depreciationReservesAmountKp;

    @ApiModelProperty(value = "核销合同收款时转回的拨备发生额")
    private BigDecimal depreciationReservesAmountSk;

    @ApiModelProperty(value = "核销合同税金计提金额")
    private BigDecimal outtaxAmount;

    @ApiModelProperty(value = "核销合同收益计提金额")
    private BigDecimal leaseRevenueAmount;

    @ApiModelProperty(value = "核销记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "实收服务费")
    private BigDecimal actualServiceAmount;

    @ApiModelProperty(value = "是否有过偿还计划变更")
    private String isChangeRepayment;

    // 实收手续费（含税）
    private BigDecimal paidHandlingFees;

    // 其它收入(含税)
    private BigDecimal otherIncome;

    // 应收租金
    private BigDecimal receivableRent;

    // 应开票/计提税额
    private BigDecimal accrualAmount;

    private BigDecimal taxAmount;
    // 应付保险费
    private BigDecimal payableInsuranceAmount;

    // 首期租金
    private BigDecimal firstRent;
    @ApiModelProperty(value = "是否服务费分摊标识")
    private Boolean sharingServiceFeeFlag;
    @ApiModelProperty(value = "设置是否服务费分摊标识时期间")
    private Integer setEndSharingServiceFeePeriod;
    @ApiModelProperty(value = "是否结束服务费分摊标识")
    private Boolean endSharingServiceFeeFlag;
    @ApiModelProperty(value = "是否特殊状态调整")
    private Boolean specialStatusAdjustmentFlag;
    @ApiModelProperty(value = "服务费历史合同标识")
    private String serviceFeeHistoryFlag;

    // 供应商手续费
    private BigDecimal receivableVendorProcedureAmount;
}
