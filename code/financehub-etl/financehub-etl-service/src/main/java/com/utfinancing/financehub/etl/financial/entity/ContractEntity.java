package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract")
public class ContractEntity extends Model<ContractEntity> {

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
    private LocalDateTime leaseDateStart;

    //到期日
    private LocalDateTime leaseDateEnd;

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
    @TableLogic
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
    private String payableDeviceAmount;

    //应收首付款
    private String receivableFirstAmount;

    //出租人保险费
    private String lessorInsuranceAmount;

    //应收承租人履约保证金
    private String receivableMarginAmount;

    //渠道费用 (=应付渠道费用+应付海通渠道费用)
    private String channelFees;

    //应付渠道费用
    private String payableChannelExpense;

    //应付海通渠道费用
    private String payableInnerExpense;

    //应收手续费收入
    private String receivableProcedureAmount;

    //出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
    private String lessorOtherCosts;

    //应付其他
    private String payableOtherAmount;

    //GPS预估费用
    private String estimateGPSExpense;

    //应付介绍费
    private String payableIntroduce;

    //应付法律费
    private String payableLawAmount;

    //应收厂商返利
    private String receivableFirmRebate;

    //应收保险费
    private String receivableInsuranceAmount;

    //名义留购价
    private String retainedPrice;

    //应收其他
    private String receivableOther;

    //应收服务费
    private String receivableServiceAmount;

    //供应商保证金
    private String vendorMarginAmount;

    //来源系统
    private String systemCode;

    //合同出单部门
    private String contractCreateDept;

    //应交销项税余额
    private String outtaxBalance;

    //租赁收益余额
    private String leaseRevenueBalance;

    //转回拨备（减值准备余额）
    private String depreciationLossBalance;

    //还款标识
    private String payMethod;

    //主合同编号
    private String contractCodeM;

    //应付手续费金额
    private String payableProcedureCost;

    //邮储项目类型
    private String postalSavingsProjectType;

    //归集财务合同状态
    private String financialContractStatusImputation;

    //gps费用单价
    private String gpsUnitPrice;

    //应付经销商服务费
    private String payableService;

    //手环成本
    private String payableBraceletCost;

    //收车费汇总总额
    private String payableRecycleCarAmount;


}
