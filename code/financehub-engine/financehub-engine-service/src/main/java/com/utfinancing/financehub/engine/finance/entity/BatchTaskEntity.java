package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 批量任务实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_batch_task")
public class BatchTaskEntity extends Model<BatchTaskEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //任务类型
    private String taskType;

    //业务id
    private Long businessId;

    //业务类型
    private String businessType;

    //任务处理数据总条数
    private Integer dataSize;

    //成功数据条数
    private Integer dataSuccessSize;

    //失败数据条数
    private Integer dataFailedSize;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

    //状态 1=处理中，2=成功，3：失败
    private String status;

    //上传用户id
    private Long userId;

    //错误信息
    private String errorInfo;

    //是否删除（0-否，1-是）
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

    //用户名
    private String userName;




}
