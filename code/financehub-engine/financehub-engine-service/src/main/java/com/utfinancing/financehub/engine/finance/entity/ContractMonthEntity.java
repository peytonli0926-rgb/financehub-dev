package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 合同月表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2025-04-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_month")
public class ContractMonthEntity extends Model<ContractMonthEntity> {

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
    private String contractAmount;

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
    private String delFlag;

    //利率
    private String leaseInterestRateYear;

    //财务合同状态更新时间
    private LocalDateTime financialContractStatusUpdateTime;

    //财务合同状态
    private String financialContractStatus;

    //系统标签
    private String systemLabel;

    //转入公司
    private String transferOrgId;

    //转入合同号
    private String transferContractCode;

    //转入合同系统合同状态
    private String transferContractStatus;

    //特殊标识
    private String specialFlag;

    //业务类型标签
    private String businessTypeLabel;

    //税率
    private String taxRate;

    //收益计算
    private String incomeCalculate;

    //收益计提方式
    private String incomeProvisionMethod;

    //开票标识
    private String invoicingFlag;

    //应付设备款
    private BigDecimal payableDeviceAmount;

    //应收首付款
    private BigDecimal receivableFirstAmount;

    //出租人保险费
    private BigDecimal lessorInsuranceAmount;

    //应收承租人履约保证金
    private String receivableMarginAmount;

    //渠道费用 (=应付渠道费用+应付海通渠道费用)
    private BigDecimal channelFees;

    //应付渠道费用
    private BigDecimal payableChannelExpense;

    //应付海通渠道费用
    private BigDecimal payableInnerExpense;

    //应收手续费收入
    private BigDecimal receivableProcedureAmount;

    //出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
    private BigDecimal lessorOtherCosts;

    //应付其他
    private BigDecimal payableOtherAmount;

    //GPS预估费用
    private BigDecimal estimateGPSExpense;

    //应付介绍费
    private BigDecimal payableIntroduce;

    //应付法律费
    private BigDecimal payableLawAmount;

    //应收厂商返利
    private BigDecimal receivableFirmRebate;

    //应收保险费
    private BigDecimal receivableInsuranceAmount;

    //名义留购价
    private BigDecimal retainedPrice;

    //应收其他
    private BigDecimal receivableOther;

    //应收服务费
    private BigDecimal receivableServiceAmount;

    //供应商保证金
    private BigDecimal vendorMarginAmount;

    //来源系统
    private String systemCode;

    //合同出单部门
    private String contractCreateDept;

    //应交销项税余额
    private BigDecimal outtaxBalance;

    //租赁收益余额
    private BigDecimal leaseRevenueBalance;

    //转回拨备（减值准备余额）
    private BigDecimal depreciationLossBalance;

    //还款标识
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

    //收车费汇总总额
    private BigDecimal payableRecycleCarAmount;

    //手环成本
    private BigDecimal payableBraceletCost;

    //业务日期
    private LocalDateTime businessDate;

    //0:非手工起租 1:手工起租
    private String manualLeaseFlag;

    //合同业务大类
    private String businessCategory;

    //省
    private String province;

    //市
    private String city;

    //租赁大表报表查询时是否过滤标志 0:过滤, 1:不过滤
    private String reportFlag;

    //是否抵债资产，0否，1是
    private String isDzzc;

    //租赁合同总计
    private String rentContractTotal;

    //期末残值
    private BigDecimal lastCost;

    //起租前已收租金
    private BigDecimal leaseBeforeReceviedAmount;

    //核销合同开票时转回的拨备发生额
    private BigDecimal depreciationReservesAmountKp;

    //核销合同收款时转回的拨备发生额
    private BigDecimal depreciationReservesAmountSk;

    //核销合同税金计提金额
    private BigDecimal outtaxAmount;

    //核销合同收益计提金额
    private BigDecimal leaseRevenueAmount;

    //记账日期
    private LocalDateTime accountDate;

    //实收服务费
    private BigDecimal actualServiceAmount;

    //是否做过偿还计划变更(0:否, 1:是)
    private String isChangeRepayment;

    //实收手续费(含税)
    private BigDecimal paidHandlingFees;

    //其它收入(含税)
    private BigDecimal otherIncome;

    //应收租金
    private BigDecimal receivableRent;

    //应开票/计提税额
    private BigDecimal accrualAmount;

    //税金计提金额
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "是否服务费分摊标识")
    private Boolean sharingServiceFeeFlag;
    @ApiModelProperty(value = "设置是否服务费分摊标识时期间")
    private Integer setEndSharingServiceFeePeriod;
    @ApiModelProperty(value = "是否结束服务费分摊标识")
    private Boolean endSharingServiceFeeFlag;

    private Boolean specialStatusAdjustmentFlag;

    @ApiModelProperty(value = "合同类别（1主合同;2咨询服务费合同;3既是主合同又是咨询服务费合同）")
    private String contractCategory;
    @ApiModelProperty(value = "服务费历史合同标识")
    private String serviceFeeHistoryFlag;

    // 供应商手续费
    private BigDecimal receivableVendorProcedureAmount;
    // 应付保险费
    private BigDecimal payableInsuranceAmount;

    // 首期租金
    private BigDecimal firstRent;
    // 费用类型801
    private BigDecimal procedure801;
}
