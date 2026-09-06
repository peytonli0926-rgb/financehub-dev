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
 * 应付保险费表详情实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_payable_insurance_details")
public class PayableInsuranceDetailsEntity extends Model<PayableInsuranceDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //应付保险费表id
    private Long payableInsuranceId;

    //业务日期
    private Date businessDate;

    //记账日期
    private Date accountDate;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //业务合同状态
    private String contractStatus;

    //财务合同状态
    private String financialContractStatus;

    //应付保险费-暂估期初余额
    private BigDecimal payableInsuranceEstimateBalanceOpening;

    //应付保险费-暂估借方
    private BigDecimal payableInsuranceEstimateAmountDebit;

    //应付保险费-暂估贷方
    private BigDecimal payableInsuranceEstimateAmountCredit;

    //应付保险费-暂估期末余额
    private BigDecimal payableInsuranceEstimateBalanceEnding;

    //应付保险费余额
    private BigDecimal payableInsuranceBalance;

    //结转金额
    private BigDecimal carryoverAmount;

    //保险费支付报表余额
    private BigDecimal payableInsuranceBalanceReport;

    //保险费实际支付（不含税）
    private BigDecimal payableInsuranceBalanceActual;

    //起租时点保险费金额（不含税）
    private BigDecimal payableInsuranceBalanceLease;

    //保险费交易结构调整（不含税）
    private BigDecimal payableInsuranceBalanceStructure;

    //合同撤销（不含税）
    private BigDecimal payableInsuranceBalanceWithdrawal;

    //凭证id
    private String voucherId;

    //是否删除 0：未删除1：已删除
    @TableLogic
    private String delFlag;

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

    // 异常信息
    private String exceptionType;
}
