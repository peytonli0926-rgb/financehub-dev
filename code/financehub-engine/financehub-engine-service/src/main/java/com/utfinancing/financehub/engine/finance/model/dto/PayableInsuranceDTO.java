package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description : 应付保险费表DTO对象
 * @Modified :
 */
@Data
public class PayableInsuranceDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private String orgId;

    @ApiModelProperty(value = "保险费结转金额")
    private BigDecimal carryoverAmount;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

}
