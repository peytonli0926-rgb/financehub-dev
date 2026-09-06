package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * @author le
 * @since 2025-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_service_fee_details_new")
public class ServiceFeeDetailsNewEntity extends Model<ServiceFeeDetailsNewEntity> {

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

    private Long serviceFeePlanId;

    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //服务费签约主体
    private String serviceOrgId;

    //服务费协议编号
    private String serviceFeeNo;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //业务合同状态
    private String contractStatus;

    //分摊分式(租赁收入分摊、服务费收入分摊)
    private String allocationMethod;

    //业务类型编码
    private String businessCode;

    //业务类型名称
    private String businessName;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //实收服务费(税前)
    private BigDecimal receivedServiceFeeTaxInclude;

    //实收服务费(税后)
    private BigDecimal receivedServiceFeeNoTax;

    //应分摊金额（税前）
    private BigDecimal shouldApportionmentAmountTaxInclude;

    //应分摊金额（税后）
    private BigDecimal shouldApportionmentAmountNoTax;

    //计划金额(税前)
    private BigDecimal planAmountTaxInclude;

    //计划金额(税后)
    private BigDecimal planAmountNoTax;

    //上月应分摊金额（税前）
    private BigDecimal lastMonthShouldApportionmentAmountTaxInclude;

    //上月应分摊金额（税后）
    private BigDecimal lastMonthShouldApportionmentAmountNoTax;

    //本月重分类调整（税前）
    private BigDecimal thisMonthReclassificationAdjustmentAmountTaxInclude;
    //本月重分类调整（税后）
    private BigDecimal thisMonthReclassificationAdjustmentAmountNoTax;
    //计提类型
    private String accrualType;

    //计提金额（税后）
    private BigDecimal accruedAmount;

    //本月以前计提类型（税后）
    private BigDecimal beforeAccruedAmount;

    //本月以后计提类型（税后）
    private BigDecimal afterAccruedAmount;

    //本期计划数（税前）
    private BigDecimal currentPeriodPlanAmountTaxInclude;

    //本期之前计划数（税前）
    private BigDecimal beforeCurrentPeriodPlanAmountTaxInclude;

    //本期之后计划数（税前）
    private BigDecimal afterCurrentPeriodPlanAmountTaxInclude;

    //分摊完结标记
    private String allocationCompletionMark;

    //异常类型
    private String exceptionType;

    //凭证id
    private String voucherId;

    //是否删除 0：未删除1：已删除
    @TableLogic
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

    //本次分摊期数
    private Integer periods;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //财务合同状态
    private String financialContractStatus;

    //计提年
    private Integer accrualYear;
    //计提月
    private Integer accrualMonth;

    //累计计提金额（不含税）
    private BigDecimal accrualAmountTotalNoTax;
    //是否跨主体分摊
    private String allocateAcrossPrincipals;
    //结束服务费分摊标识
    private String endSharingServiceFeeFlag;
    //分摊服务费标识
    private String sharingServiceFeeFlag;
    //是否特殊状态调整
    private String specialStatusAdjustmentFlag;


}
