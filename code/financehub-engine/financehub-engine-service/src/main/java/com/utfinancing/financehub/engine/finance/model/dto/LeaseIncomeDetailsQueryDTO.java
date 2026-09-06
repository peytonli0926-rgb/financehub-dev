package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
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
 * @Date : Create in 2023-11-13
 * @Description :   LeaseIncomeDetails查询from对象
 * @Modified :
 */
@ApiModel("LeaseIncomeDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaseIncomeDetailsQueryDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "计提月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private String businessDate;
    @ApiModelProperty(value = "开始月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessStartDate;
    @ApiModelProperty(value = "截止月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessEndDate;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "租赁收益表id")
    private Long leaseIncomeId;
    private List<Long> leaseIncomeIdList;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;
    private List<String> systemCodeList;


    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    private List<String> orgIdList;

    @ApiModelProperty(value = "业务大类")
    private String businessType;
    private List<String> businessTypeList;

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

    @ApiModelProperty(value = "异常情况提示")
    private String exceptionType;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "凭证id")
    private String voucherId;

    @ApiModelProperty(value = "特殊合同状态标识")
    private String specialContractFlag;

    @ApiModelProperty(value = "特殊合同状态")
    private List<String> financialContractStatusList;

    @ApiModelProperty(value = "计提凭证状态")
    private String voucherStatus;

    @ApiModelProperty(value = "还款情况变化提示")
    private String repaymentSituation;

    @ApiModelProperty(value = "交易结构手工调整标志")
    private String manualChangeMark;

    @ApiModelProperty(value = "abs赎回标志")
    private String absMark;

    @ApiModelProperty(value = "手工起租标志")
    private String manualLease;

    @ApiModelProperty(value = "还款情况")
    private String repaymentFlag;

    @ApiModelProperty(value = "最后还款日")
    private Date lastRepaymentDate;

    @ApiModelProperty(value = "合同编码集合")
    private List<String> contractCodeList;
}
