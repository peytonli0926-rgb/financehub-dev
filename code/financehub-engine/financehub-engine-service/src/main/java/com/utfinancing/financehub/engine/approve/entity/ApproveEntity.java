package com.utfinancing.financehub.engine.approve.entity;

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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_approve")
public class ApproveEntity extends Model<ApproveEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //单据id
    @ApiModelProperty(value = "单据id")
    private Long documentId;

    //单据类型
    @ApiModelProperty(value = "单据类型")
    private String documentType;

    //单据状态
    @ApiModelProperty(value = "单据状态")
    private String documentStatus;

    //提交人工号
    @ApiModelProperty(value = "提交人工号")
    private String submitterNum;

    //提交人姓名
    @ApiModelProperty(value = "提交人姓名")
    private String submitterName;

    //审批人工号
    @ApiModelProperty(value = "审批人工号")
    private String approverNum;

    //审批人姓名
    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    //提交时间
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitDate;

    //审批时间
    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approverDate;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    //审批备注
    @ApiModelProperty(value = "审批备注")
    private String remark;

    //是否删除（0：否，1：是）
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


}
