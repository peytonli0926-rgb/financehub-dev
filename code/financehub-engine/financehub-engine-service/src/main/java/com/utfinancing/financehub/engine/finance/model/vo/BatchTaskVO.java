package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-06-25
 * @Description : 批量任务VO对象
 * @Modified :
 */
@Data
public class BatchTaskVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "任务处理数据总条数")
    private Integer dataSize;

    @ApiModelProperty(value = "成功数据条数")
    private Integer dataSuccessSize;

    @ApiModelProperty(value = "失败数据条数")
    private Integer dataFailedSize;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "状态 1=处理中，2=成功，3：失败")
    private String status;

    @ApiModelProperty(value = "上传用户id")
    private Long userId;

    @ApiModelProperty(value = "错误信息")
    private String errorInfo;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "用户名")
    private String userName;


}
