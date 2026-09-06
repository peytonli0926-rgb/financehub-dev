package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :   ServiceFeeDetails查询from对象
 * @Modified :
 */
@ApiModel("ServiceFeeDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFeeDetailsQueryDTO extends BaseQueryDTO {

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

    @ApiModelProperty(value = "服务费分摊表id")
    private Long serviceFeeId;
    private List<Long> serviceFeeIdList;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    private List<String> orgIdList;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;
    private List<String> serviceOrgIdList;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "分摊方式(租赁收入分摊、服务费收入分摊)")
    private String allocationMethod;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "起租日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "服务费实收（税后）")
    private BigDecimal serviceFeeReceived;

    @ApiModelProperty(value = "应分摊的服务费收入（税后）")
    private BigDecimal serviceFeeAllocationNoTax;

    @ApiModelProperty(value = "上月服务费应分摊金额（税后）")
    private BigDecimal lastMonthServiceFeeAllocationNoTax;

    @ApiModelProperty(value = "本月重分类调整(税后)")
    private BigDecimal reclassificationAdjustmentNoTaxAmount;

    @ApiModelProperty(value = "计提类型")
    private String accrualType;

    @ApiModelProperty(value = "本期以前")
    private BigDecimal beforeXYearMonthAmount;

    @ApiModelProperty(value = "本期调整")
    private BigDecimal xYearMonthAdjustmentAmount;

    @ApiModelProperty(value = "本期摊销后余额")
    private BigDecimal allocationAfterXYearMonthBalance;

    @ApiModelProperty(value = "实际未计提金额")
    private BigDecimal notAccruedAmount;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "本次分摊期数")
    private Integer periods;

    @ApiModelProperty(value = "特殊合同状态")
    private String financialContractStatus;
    private List<String> financialContractStatusList;

    @ApiModelProperty(value = "累计已计提金额")
    private BigDecimal accumulatedAccruedAmount;

    @ApiModelProperty(value = "分摊完结标记")
    private String allocationCompletionMark;

    @ApiModelProperty(value = "异常提示")
    private List<String> exceptionTypeList;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "业务大类")
    private List<String> businessTypeList;

    @ApiModelProperty(value = "所属系统")
    private List<String> systemCodeList;

    @ApiModelProperty(value = "计提凭证状态")
    private String voucherStatus;
}
