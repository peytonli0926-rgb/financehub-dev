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
import lombok.*;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author le
 * @since 2025-11-10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("eg_service_fee_plan_new")
public class ServiceFeePlanNewEntity extends Model<ServiceFeePlanNewEntity> {

    private static final long serialVersionUID = 1L;

    //主键ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编码
    private String contractCode;

    //组织机构ID
    private String orgId;

    //服务费编号
    private String serviceFeeNo;

    //服务方组织机构ID
    private String serviceOrgId;

    //分摊比例
    private BigDecimal apportionmentRate;

    //计提金额
    private BigDecimal accruedAmount;

    //调整金额
    private BigDecimal adjustAmount;

    //删除标志（0代表存在 1代表删除）
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

    //计划日期
    private Date planDate;

    //应收服务费金额(税前)
    private BigDecimal receivableServiceFeeTaxInclude;

    //应收服务费金额(税后)
    private BigDecimal receivableServiceFeeNoTax;

    //实收服务费(税前)
    private BigDecimal receivedServiceFeeTaxInclude;

    //实收服务费(税后)
    private BigDecimal receivedServiceFeeNoTax;

    //应分摊金额(税前)
    private BigDecimal shouldApportionmentAmountTaxInclude;

    //应分摊金额(税后)
    private BigDecimal shouldApportionmentAmountNoTax;

    //期数
    private Integer periods;

    //计划金额(税前)
    private BigDecimal planAmountTaxInclude;

    //不含税计划金额(税后)
    private BigDecimal planAmountNoTax;
    //累计计划金额(税前)
    private BigDecimal planAmountTotalTaxInclude;
    //累计计划金额(税后)
    private BigDecimal planAmountTotalNoTax;
    //计提比例
    private BigDecimal accrualRate;


}
