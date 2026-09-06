package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 服务费分摊表详情实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_service_fee_details")
public class ServiceFeeDetailsEntity extends Model<ServiceFeeDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //记账日期
    private Date accountDate;

    //业务日期
    private Date businessDate;

    //服务费分摊表id
    private Long serviceFeeId;

    //合同编号
    private String contractCode;

    //服务费协议编号
    private String serviceFeeNo;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //服务费签约主体
    private String serviceOrgId;

    //业务合同状态
    private String contractStatus;

    //分摊方式(租赁收入分摊、服务费收入分摊)
    private String allocationMethod;

    // 分摊比例
    private BigDecimal allocationRatio;

    //业务类型编码
    private String businessCode;

    //业务类型名称
    private String businessName;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //服务费实收（税后）
    private BigDecimal serviceFeeReceived;

    //应分摊的服务费收入（税后）
    private BigDecimal serviceFeeAllocationNoTax;

    //上月服务费应分摊金额（税后）
    private BigDecimal lastMonthServiceFeeAllocationNoTax;

    //本月重分类调整(税后)
    private BigDecimal reclassificationAdjustmentNoTaxAmount;

    //计提类型
    private String accrualType;

    //本期以前
    private BigDecimal beforeXYearMonthAmount;

    //本期调整
    private BigDecimal xYearMonthAdjustmentAmount;

    //本期摊销后余额
    private BigDecimal allocationAfterXYearMonthBalance;

    //实际未计提金额
    private BigDecimal notAccruedAmount;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //本次分摊期数
    private Integer periods;

    //财务合同状态
    private String financialContractStatus;

    //累计已计提金额
    private BigDecimal accumulatedAccruedAmount;

    //分摊完结标记
    private String allocationCompletionMark;

    //异常类型
    private String exceptionType;

    //凭证id
    private String voucherId;

    //是否删除 0：未删除1：已删除
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "服务费实收（税前）")
    private BigDecimal serviceFeeReceivedTaxIncluded;

    @ApiModelProperty(value = "应分摊的服务费收入（税前）")
    private BigDecimal serviceFeeAllocationTaxIncluded;

    @ApiModelProperty(value = "上月服务费应分摊金额（税前）")
    private BigDecimal lastMonthServiceFeeAllocationTaxIncluded;

    @ApiModelProperty(value = "本月重分类调整(税前)")
    private BigDecimal reclassificationAdjustmentTaxIncluded;

    @ApiModelProperty(value = "本月以前（税前）")
    private BigDecimal beforeThisMonthAmountTaxIncluded;

    @ApiModelProperty(value = "本月调整（税前）")
    private BigDecimal thisMonthAdjustmentAmountTaxIncluded;

    @ApiModelProperty(value = "本月摊销后余额（税前）")
    private BigDecimal allocationBeforeThisMonthBalanceTaxIncluded;

    @ApiModelProperty(value = "本月以前（税后）")
    private BigDecimal beforeThisMonthAmountNoTax;

    @ApiModelProperty(value = "本月调整（税后）")
    private BigDecimal thisMonthAdjustmentAmountNoTax;

    @ApiModelProperty(value = "本月摊销后余额（税后）")
    private BigDecimal allocationBeforeThisMonthBalanceNoTax;
//
//    @ApiModelProperty(value = "本期之前（税前）")
//    private BigDecimal beforePeriodAmountTaxIncluded;
//
//    @ApiModelProperty(value = "本期发生（税前）")
//    private BigDecimal currentPeriodAmountTaxIncluded;
//
//    @ApiModelProperty(value = "本期之后（税前）")
//    private BigDecimal afterPeriodAmountTaxIncluded;
//
//    @ApiModelProperty(value = "计提凭证状态")
//    private String voucherStatus;
//
//    @ApiModelProperty(value = "是否分摊标记")
//    private String sharedFlag;
//
//    @ApiModelProperty(value = "是否特殊状态调整")
//    private String specialStatusAdjustmentFlag;
}
