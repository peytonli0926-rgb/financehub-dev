package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-26
 * @Description :   ChargeOffSummaryReport查询from对象
 * @Modified :
 */
@ApiModel("ChargeOffSummaryReport查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChargeOffSummaryReportQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "核销状态")
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "核销时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty(value = "财务核销敞口")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "拨备转回金额")
    private BigDecimal provisionReversalAmount;

    @ApiModelProperty(value = "拨备转回年份")
    private String provisionReversalYear;

    @ApiModelProperty(value = "税务核销日期")
    private Date taxVerificationDate;

    @ApiModelProperty(value = "税务核销金额")
    private BigDecimal taxVerificationAmount;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "核销状态集合")
    private List<String> verificationStatusList;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "核销开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startVerificationDate;

    @ApiModelProperty(value = "核销结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endVerificationDate;

    @ApiModelProperty(value = "税务核销开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTaxVerificationDate;

    @ApiModelProperty(value = "税务核销结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endTaxVerificationDate;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("0：新增模板，1：修改模板")
    private String type;

    @ApiModelProperty("上个月会计期间")
    private Integer lastPeriodCode;

}
