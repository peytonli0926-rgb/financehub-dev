package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_ta_amount")
public class ContractTaAmountEntity extends Model<ContractTaAmountEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编码
    private String contractCode;

    //ta金额
    private BigDecimal taAmount;

    //传送日期
    private Date transactionDate;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String delFlag;

    private String orgId;
}
