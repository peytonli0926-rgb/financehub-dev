package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 金蝶202311期合同的借方金额实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_voucher_amount_init")
public class VoucherAmountInitEntity extends Model<VoucherAmountInitEntity> {

    private static final long serialVersionUID = 1L;

    //主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //金蝶主键
    private String fid;

    //签约主体
    private String orgId;

    //期间
    private String period;

    //合同编号
    private String contractCode;

    //客户编号
    private String clientCode;

    //科目编号
    private String accountNumber;

    //科目名称
    private String accountName;

    private String fabStract;

    //借方金额
    private BigDecimal dtAmount;

    //贷方金额
    private BigDecimal crAmount;


}
