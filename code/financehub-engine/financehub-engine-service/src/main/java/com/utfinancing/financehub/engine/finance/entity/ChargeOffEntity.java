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
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Charge Off手工上传表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_charge_off")
public class ChargeOffEntity extends Model<ChargeOffEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //核销状态
    private String verificationStatus;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //核销时间
    private LocalDateTime verificationDate;

    //财务核销敞口
    private BigDecimal financialExpenseAmount;

    //拨备转回金额
    private BigDecimal provisionReversalAmount;

    //拨备转回年份
    private String provisionReversalYear;

    //税务核销日期
    private LocalDateTime taxVerificationDate;

    //税务核销金额
    private BigDecimal taxVerificationAmount;

    //流程实例id
    private Long processInstanceId;

    //1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
    private String processStatus;

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
    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "坏账核销余额")
    private BigDecimal badDebtWriteOffBalance;


}
