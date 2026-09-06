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
import lombok.*;

/**
 * <p>
 * 服务费计划表实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("eg_service_fee_plan")
public class ServiceFeePlanEntity extends Model<ServiceFeePlanEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //服务费协议编号
    private String serviceFeeNo;

    //服务费签约主体
    private String serviceOrgId;

    //服务费摊销利率
    private BigDecimal serviceFeeAmortizationRate;

    //实际计提金额
    private BigDecimal actualAccruedAmount;

    //调整金额
    private BigDecimal adjustAmount;

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

    //计划日期
    private Date planDate;
    @ApiModelProperty(value = "计提类型")
    private String accrualType;
    //服务费总额
    private BigDecimal serviceFeeTotal;
    //服务费总额
    private BigDecimal serviceFeeTotalNoTax;

    //服务费协议金额
    private BigDecimal serviceFeeAgreedAmount;

    //实收服务费金额
    private BigDecimal actualReceiveServiceFee;

    //合同一次性确认金额
    private BigDecimal contractConfirmedAmount;

    //协议一次性确认金额
    private BigDecimal agreedConfirmedAmount;

    //协议分摊金额
    private BigDecimal agreedApportionAmount;

    //合同设备金额
    private BigDecimal contractDeviceAmount;

    //计划分摊金额
    private BigDecimal planApportionAmount;

    //期数
    private Integer periods;

    //计划分摊金额（税后）
    private BigDecimal planApportionNoTax;

    //本期计划计提金额（税前）
    private BigDecimal planAmount;
    //本期计划计提金额（税后）
    private BigDecimal planAmountNoTax;
    //服务费实收（税后）
    private BigDecimal actualReceiveNoTax;
}
