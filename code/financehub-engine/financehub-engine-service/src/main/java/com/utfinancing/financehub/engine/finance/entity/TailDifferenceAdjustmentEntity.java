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
import lombok.*;

/**
 * <p>
 * 尾差调整实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("eg_tail_difference_adjustment")
public class TailDifferenceAdjustmentEntity extends Model<TailDifferenceAdjustmentEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //签约主体
    private String orgId;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期
    private LocalDateTime accountDate;

    //流程id
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

    @ApiModelProperty("审批报错信息")
    private String approveErrorInfo;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

}
