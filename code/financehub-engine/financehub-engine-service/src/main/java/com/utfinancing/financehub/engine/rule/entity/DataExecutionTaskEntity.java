package com.utfinancing.financehub.engine.rule.entity;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 业务系统数据执行任务表实体对象
 * </p>
 *
 * @author lixin
 * @since 2024-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_data_execution_task")
public class DataExecutionTaskEntity extends Model<DataExecutionTaskEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //系统编码
    private String systemCode;

    //任务状态
    private String status;

    //任务开始时间
    private LocalDateTime taskStartTime;

    //任务结束时间
    private LocalDateTime taskEndTime;

    //业务日期开始时间
    private LocalDateTime businessDateStart;

    //业务日期结束时间
    private LocalDateTime businessDateEnd;

    //任务处理数据总条数
    private Integer dataSize;

    //成功数据条数
    private Integer dataSuccessSize;

    //失败数据条数
    private Integer dataFailedSize;

    //任务异常消息
    private String errorMessage;

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


}
