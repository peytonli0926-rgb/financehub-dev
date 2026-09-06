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
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :   ServiceFee查询from对象
 * @Modified :
 */
@ApiModel("ServiceFee查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFeeQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "计提月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessDate;
    @ApiModelProperty(value = "开始月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessStartDate;
    @ApiModelProperty(value = "截止月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessEndDate;
    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "服务费摊销收入")
    private BigDecimal serviceFeeAmortizationIncome;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;
    private String voucherStatus;

    @ApiModelProperty(value = "处理状态列表")
    private List<String> processStatusList;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "提交人")
    private String submitBy;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private List<String> orgIdList;
    private List<String> serviceOrgIdList;

    @ApiModelProperty(value = "分摊比例")
    private BigDecimal allocationRatio;


    private List<Long> idList;

    @ApiModelProperty(value = "ID")
    private Long id;


    @ApiModelProperty(value = "是否跨主体分摊")
    private String allocateAcrossPrincipals;


    @ApiModelProperty(value = "追溯月份开始")
    private Date retraceMonthStart;

    @ApiModelProperty(value = "追溯月份结束")
    private Date retraceMonthEnd;



}
