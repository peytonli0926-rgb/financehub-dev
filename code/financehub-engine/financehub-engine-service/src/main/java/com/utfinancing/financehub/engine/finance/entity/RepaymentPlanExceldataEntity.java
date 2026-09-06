package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_repayment_plan_exceldata")
public class RepaymentPlanExceldataEntity extends Model<RepaymentPlanExceldataEntity> {

    private static final long serialVersionUID = 1L;

    private String systemCode;

    private String contractCode;

    private Integer periods;

    private Date planDate;

    //实际归还租金
    private BigDecimal actualRepaymentRentAmount;

    //实际归还本金
    private BigDecimal actualRepaymentPrincipalAmount;

    //实际归还利息
    private BigDecimal actualRepaymentInteresAmount;

    //TA金额
    private BigDecimal ta;

    @ApiModelProperty("网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("结算方式")
    private String settlementWay;
}
