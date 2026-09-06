package com.utfinancing.financehub.engine.rule.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 * @Description : MQ异常消息记录表DTO对象
 * @Modified :
 */
@Data
public class MqErrorMessageDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "消息内容")
    private String messageBody;

    @ApiModelProperty(value = "消息处理状态 0:未处理 1:已重推  2:已忽略")
    private String status;

    @ApiModelProperty(value = "原始路由key")
    private String originalRoutingKey;

    @ApiModelProperty(value = "原始交换机")
    private String originalExchange;

    @ApiModelProperty(value = "异常消息")
    private String exceptionMessage;

    @ApiModelProperty(value = "异常堆栈")
    private String exceptionStacktrace;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
