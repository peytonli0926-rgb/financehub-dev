package com.utfinancing.financehub.engine.rule.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-02-25
 * @Description : 业务系统数据执行任务表DTO对象
 * @Modified :
 */
@Data
public class DataExecutionTaskDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime taskStartTime;

    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime taskEndTime;

    @ApiModelProperty(value = "业务日期开始时间")
    private LocalDateTime businessDateStart;

    @ApiModelProperty(value = "业务日期结束时间")
    private LocalDateTime businessDateEnd;

    @ApiModelProperty(value = "任务处理数据总条数")
    private Integer dataSize;

    @ApiModelProperty(value = "成功数据条数")
    private Integer dataSuccessSize;

    @ApiModelProperty(value = "失败数据条数")
    private Integer dataFailedSize;

    @ApiModelProperty(value = "任务异常消息")
    private String errorMessage;

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
