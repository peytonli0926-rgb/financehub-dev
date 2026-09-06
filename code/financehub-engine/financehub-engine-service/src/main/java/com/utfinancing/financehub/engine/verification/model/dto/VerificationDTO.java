package com.utfinancing.financehub.engine.verification.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-11
 * @Description : 核销表DTO对象
 * @Modified :
 */
@Data
public class VerificationDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private String orgId;

    @ApiModelProperty(value = "核销状态")
    private String verificationStatus;

    @ApiModelProperty(value = "财务报销敞口金额")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "补偿提备金额")
    private BigDecimal compensationProvisionAmount;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

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

    @ApiModelProperty(value = "核销id集合")
    private List<Long> idList;

}
