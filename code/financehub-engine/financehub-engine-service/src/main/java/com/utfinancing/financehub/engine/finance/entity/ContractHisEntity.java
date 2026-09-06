package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同历史表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_his")
public class ContractHisEntity extends Model<ContractHisEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;
    //主合同编号
    private String contractCodeM;

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
    private BigDecimal contractAmount;

    //利率
    private BigDecimal leaseInterestRateYear;

    //财务合同状态更新时间
    private Date financialContractStatusUpdateTime;

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
    private BigDecimal taxRate;

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
    private BigDecimal receivableMarginAmount;

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

    //处理状态
    private String processStatus;

    //提交人
    private String submitBy;

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

    //是否已生成交易结构凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //是否已生成租金计划凭证（0：未生成1：已生成）默认0
    private String isPlanVoucher;

    //交易结构凭证id todo 需要手工凭证返回
    private String voucherId;

    //租金计划凭证id
    private Long planVoucherId;

    // 异常信息
    private String exceptionType;

    // 流程id
    private Long processInstanceId;

    // 原组织编码
    private String oldOrgId;
}
