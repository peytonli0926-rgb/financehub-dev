package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_service_no_amortization")
public class ServiceNoAmortizationEntity extends Model<ServiceNoAmortizationEntity> {

    private static final long serialVersionUID = 1L;

    private String contractCode;

    private BigDecimal serviceNoAmortizationAmout;


}
