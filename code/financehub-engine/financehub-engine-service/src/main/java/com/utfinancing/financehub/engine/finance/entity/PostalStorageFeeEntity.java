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
 * 邮储手续费实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_postal_storage_fee")
public class PostalStorageFeeEntity extends Model<PostalStorageFeeEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //记账日期
    private Date accountDate;

    //业务日期
    private Date businessDate;

    //签约主体（组织机构编码）
    private String orgId;

    //支付手续费金额
    private BigDecimal payableProcedureCost;

    //当期分摊金额
    private BigDecimal allocationAmount;

    //分摊余额
    private BigDecimal allocationBalance;

    //处理状态
    private String processStatus;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //是否删除（0：未删除1：删除）默认0
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

    //提交人
    private String submitBy;


}
