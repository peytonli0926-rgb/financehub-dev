package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-28
 * @Description : 减值计提上传任务DTO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionUploadTaskDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "导入文件名称")
    private String fileName;

    @ApiModelProperty(value = "状态，0-开始;1,结束")
    private String status;

    @ApiModelProperty(value = "上传用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

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

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "任务处理数据总条数")
    private Integer dataSize;

    @ApiModelProperty(value = "成功数据条数")
    private Integer dataSuccessSize;

    @ApiModelProperty(value = "失败数据条数")
    private Integer dataFailedSize;

    @ApiModelProperty(value = "单据id")
    private Long docId;

}
