package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_account_middle_tmp")
public class CheckAccountMiddleTmpEntity extends Model<CheckAccountMiddleTmpEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String businessDate;

    private String voucherDate;

    private String systemCode;

    private Integer periodCode;

    private String currencyType;

    private String orgId;

    private String accountCode;

    private String clientCode;

    private String contractCode;

    private String bankAccount;

    private String billContractCode;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;


}
