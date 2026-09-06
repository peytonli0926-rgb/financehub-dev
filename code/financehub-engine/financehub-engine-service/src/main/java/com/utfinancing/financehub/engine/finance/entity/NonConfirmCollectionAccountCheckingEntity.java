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
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_non_confirm_collection_account_checking")
public class NonConfirmCollectionAccountCheckingEntity extends Model<NonConfirmCollectionAccountCheckingEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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

    //对账月份
    private String accountCheckingMonth;

    //到账主体
    private String collectionAccountsBank;

    //系统编码
    private String systemCode;

    //业务系统的网银编号
    private String businessEbankNumber;

    //业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
    private String ebankSerialNumber;

    //入账日期
    private LocalDateTime businessHappenDate;

    //账龄分类
    private String accountAgeClass;

    // 账龄
    private BigDecimal accountAge;

    //月初余额
    private BigDecimal monthInitBalance;

    //本月贷方发生额
    private BigDecimal curMonthCreditAmount;

    //本月余额
    private BigDecimal curMonthBalance;

    //系统金额
    private BigDecimal systemAmount;

    //差额
    private BigDecimal diffAmount;

    //财务对账备注
    private String accountCheckingComments;

    //财务初分类
    private String financialPrimaryClassic;

    //运营部确认款项性质
    private String confirmAccountProperty;

    //是否已经确认
    private String isConfirmed;

    // 到账主体编码
    private String collectionAccountsBankCode;

    // comments
    private String remark;

    // 非租对账备注
    private String nonLeaseAccountCheckingComments;

    // 非租未认领金额
    private BigDecimal nonLeaseNonClaimAmount;

    // 非租未认领原因
    private String nonLeaseNonClaimReasons;

    private BigDecimal reclassAmount;

    private String reclassAccountNumber;

    private String operateHistoryComments;
}
