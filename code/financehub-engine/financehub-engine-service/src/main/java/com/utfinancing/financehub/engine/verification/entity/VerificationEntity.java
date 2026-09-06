package com.utfinancing.financehub.engine.verification.entity;

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
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 核销表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_verification")
public class VerificationEntity extends Model<VerificationEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //记账日期
    private LocalDateTime accountDate;

    //签约主体（组织机构编码）
    private String orgId;

    //核销状态
    private String verificationStatus;

    //财务报销敞口金额
    private BigDecimal financialExpenseAmount;

    //补偿提备金额
    private BigDecimal compensationProvisionAmount;

    //处理状态
    private String processStatus;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //流程实例id
    private Long processInstanceId;

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

    //会计期间
    private Integer periodCode;

    @ApiModelProperty("创建人姓名")
    private String createName;



}
