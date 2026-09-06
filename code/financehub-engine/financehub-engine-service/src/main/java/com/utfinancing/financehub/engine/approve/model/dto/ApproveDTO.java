package com.utfinancing.financehub.engine.approve.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-08
 * @Description : 审批表DTO对象
 * @Modified :
 */
@Data
public class ApproveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "单据id")
    @NotNull(message = "单据Id不可以为空")
    private Long documentId;

    @ApiModelProperty(value = "单据类型")
    @NotNull(message = "单据类型不可以为空")
    private String documentType;

    @ApiModelProperty(value = "单据状态")
    private String documentStatus;

    @ApiModelProperty(value = "提交人工号")
    private String submitterNum;

    @ApiModelProperty(value = "提交人姓名")
    private String submitterName;

    @ApiModelProperty(value = "审批人工号")
    private String approverNum;

    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitDate;

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approverDate;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "审批备注")
    private String remark;

    @ApiModelProperty(value = "是否删除（0：否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
