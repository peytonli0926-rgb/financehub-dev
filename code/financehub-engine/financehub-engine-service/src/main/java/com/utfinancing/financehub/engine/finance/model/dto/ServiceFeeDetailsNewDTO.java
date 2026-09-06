package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description : 服务费分摊表详情DTO对象
 * @Modified :
 */
@Data
public class ServiceFeeDetailsNewDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "服务费分摊表id")
    private Long serviceFeeId;

    @ApiModelProperty(value = "")
    private Long serviceFeePlanId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "分摊分式(租赁收入分摊、服务费收入分摊)")
    private String allocationMethod;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "起租日")
    private LocalDateTime leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "实收服务费(税前)")
    private BigDecimal receivedServiceFeeTaxInclude;

    @ApiModelProperty(value = "实收服务费(税后)")
    private BigDecimal receivedServiceFeeNoTax;

    @ApiModelProperty(value = "应分摊金额（税前）")
    private BigDecimal shouldApportionmentAmountTaxInclude;

    @ApiModelProperty(value = "应分摊金额（税后）")
    private BigDecimal shouldApportionmentAmountNoTax;

    @ApiModelProperty(value = "计划金额(税前)")
    private BigDecimal planAmountTaxInclude;

    @ApiModelProperty(value = "计划金额(税后)")
    private BigDecimal planAmountNoTax;

    @ApiModelProperty(value = "上月应分摊金额（税前）")
    private BigDecimal lastMonthShouldApportionmentAmountTaxInclude;

    @ApiModelProperty(value = "上月应分摊金额（税后）")
    private BigDecimal lastMonthShouldApportionmentAmountNoTax;

    @ApiModelProperty(value = "本月重分类调整（税前）")
    private BigDecimal thisMonthReclassificationAdjustmentAmountTaxInclude;
    @ApiModelProperty(value = "本月重分类调整（税后）")
    private BigDecimal thisMonthReclassificationAdjustmentAmountNoTax;
    @ApiModelProperty(value = "计提类型")
    private String accrualType;

    @ApiModelProperty(value = "计提金额（税后）")
    private BigDecimal accruedAmount;

    @ApiModelProperty(value = "本月以前计提类型（税后）")
    private BigDecimal beforeAccruedAmount;

    @ApiModelProperty(value = "本月以后计提类型（税后）")
    private BigDecimal afterAccruedAmount;

    @ApiModelProperty(value = "本期计划数（税前）")
    private BigDecimal currentPeriodPlanAmountTaxInclude;

    @ApiModelProperty(value = "本期之前计划数（税前）")
    private BigDecimal beforeCurrentPeriodPlanAmountTaxInclude;

    @ApiModelProperty(value = "本期之后计划数（税前）")
    private BigDecimal afterCurrentPeriodPlanAmountTaxInclude;

    @ApiModelProperty(value = "分摊完结标记")
    private String allocationCompletionMark;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

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

    @ApiModelProperty(value = "本次分摊期数")
    private Integer periods;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "计提年")
    private Integer accrualYear;
    @ApiModelProperty(value = "计提月")
    private Integer accrualMonth;

    @ApiModelProperty(value = "累计计提金额（不含税）")
    private String accrualAmountTotalNoTax;

    //结束服务费分摊标识
    private String endSharingServiceFeeFlag;
    //分摊服务费标识
    private String sharingServiceFeeFlag;
    //是否特殊状态调整
    private String specialStatusAdjustmentFlag;
}
