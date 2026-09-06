package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_hy_full_online_bank_batch_no_mapping")
public class HyFullOnlineBankBatchNoMappingEntity extends Model<HyFullOnlineBankBatchNoMappingEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //业务系统网银编号
    private String onlineBankNo;

    //批扣流水号
    private String deductBatchNo;

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

    //是否删除（0:否，1：是）
    private String delFlag;

    //扣除种类:1手动汇款、2自动扣款
    private String deductCategory;

    //乘用、商用
    private String businessLine;

    //银行回单号
    private String bankReceiptNo;

    //到账时间
    private LocalDateTime collectAmountTime;

    //到账金额
    private BigDecimal collectAmount;

    //到账银行
    private String collectBank;

    //到账银行账号
    private String collectBankAccount;

    //摘要
    private String comments;

    //付款账户
    private String payBankAccountName;

    //付款银行
    private String payBank;

    //付款银行账户
    private String payBankAccount;

    //可核销金额
    private BigDecimal canChargeOffAmount;

    //已核销金额
    private BigDecimal alreadyChargeOffAmount;

    //已认领溢存款待核销金额。认领到溢存款时增加，溢存款出账时减少
    private BigDecimal decDongjje;

    //退款金额
    private BigDecimal refundAmount;

    //到账账户
    private String collectAmountAccountName;

    //退款标志
    private String refundFlag;

    //网银归属 01-C0001:恒信 30001:自贸区
    private String onlineBankBelong;

    private Long sumId;

    @ApiModelProperty("客户编码")
    private String clientCode;

    @ApiModelProperty("客户名称")
    private String clientName;
}
