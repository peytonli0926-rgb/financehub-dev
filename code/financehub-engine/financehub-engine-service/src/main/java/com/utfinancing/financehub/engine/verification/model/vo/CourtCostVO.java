package com.utfinancing.financehub.engine.verification.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description : 诉讼费转费用表VO对象
 * @Modified :
 */
@Data
public class CourtCostVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate accountDate;

    @ApiModelProperty(value = "转费用金额")
    private BigDecimal transgerCostAmount;

    @ApiModelProperty(value = "处理状态(1-已录入，2-已提交，3-已复核，4-已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

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

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty("流程实例id")
    private Long processInstanceId;

}
