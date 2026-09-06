package com.utfinancing.financehub.engine.verification.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-11
 * @Description :   Verification查询from对象
 * @Modified :
 */
@ApiModel("Verification查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate accountDate;

    @ApiModelProperty(value = "记账开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startAccountDate;

    @ApiModelProperty(value = "记账结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endAccountDate;

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

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty("id")
    private Long id;
}
