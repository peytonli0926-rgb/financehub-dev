package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同未实现收益的初始化数据
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_outstanding_amount_init")
public class OutstandingAmountInitEntity extends Model<OutstandingAmountInitEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    // 合同编号
    private String contractCode;

    // 科目编号
    private String accountNumber;

    // 科目名称
    private String accountName;

    // 客户编号
    private String clientCode;

    // 签约主体
    private String orgId;

    // 金额
    private String endBalanceFor;
}
