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
 * MQ异常消息记录表实体对象
 * </p>
 *
 * @author lixin
 * @since 2024-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_mq_error_message")
public class MqErrorMessageEntity extends Model<MqErrorMessageEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //消息内容
    private String messageBody;

    //消息处理状态 0:未处理 1:已重推  2:已忽略
    private String status;

    //原始路由key
    private String originalRoutingKey;

    //原始交换机
    private String originalExchange;

    //异常消息
    private String exceptionMessage;

    //异常堆栈
    private String exceptionStacktrace;

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
