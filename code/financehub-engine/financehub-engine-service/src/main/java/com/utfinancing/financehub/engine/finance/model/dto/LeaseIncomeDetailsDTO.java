package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-13
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class LeaseIncomeDetailsDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "计提月份")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "租赁收益表id")
    private Long leaseIncomeId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "业务大类")
    private String businessType;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "财务IRR")
    private BigDecimal xirrRate;

    @ApiModelProperty(value = "未实现收益总额")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "本月以前")
    private BigDecimal rentalIncomeBeforeTotal;

    @ApiModelProperty(value = "本月")
    private BigDecimal rentalIncome;

    @ApiModelProperty(value = "本月之后")
    private BigDecimal rentalIncomeAfterTotal;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "逾期收益")
    private BigDecimal overdueEarnings;

    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "上期实收期间")
    private Date previousPaidPeriod;

    @ApiModelProperty(value = "当月逾期调整额")
    private BigDecimal overdueAdjustmentAmount;

    @ApiModelProperty(value = "合计入账金额")
    private BigDecimal totalRecordedAmount;

    @ApiModelProperty(value = "实收-已确认")
    private BigDecimal confirmedActualReceipt;

    @ApiModelProperty(value = "是否计提")
    private String accrued;

    @ApiModelProperty(value = "计提方式")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "凭证id")
    private String voucherId;

    @ApiModelProperty(value = "是否删除 0：未删除1：已删除")
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
