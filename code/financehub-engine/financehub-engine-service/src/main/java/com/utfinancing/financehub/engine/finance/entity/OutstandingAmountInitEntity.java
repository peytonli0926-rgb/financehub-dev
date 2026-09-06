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
 * @since 2025-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_outstanding_amount_init")
public class OutstandingAmountInitEntity extends Model<OutstandingAmountInitEntity> {

    private static final long serialVersionUID = 1L;

    //主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //科目编号
    private String accountNumber;

    //科目名称
    private String accountName;

    //客户编号
    private String clientCode;

    //签约主体
    private String orgId;

    //金额
    private BigDecimal endBalanceFor;


}
