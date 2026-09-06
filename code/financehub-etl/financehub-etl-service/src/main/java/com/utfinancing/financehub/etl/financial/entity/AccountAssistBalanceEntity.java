package com.utfinancing.financehub.etl.financial.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 科目辅助帐余额表实体对象
 * </p>
 *
 * @author lixin
 * @since 2024-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_account_assist_balance")
public class AccountAssistBalanceEntity extends Model<AccountAssistBalanceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //会计期间
    private Integer periodCode;

    //合同编号
    private String contractCode;

    //客户编号
    private String clientCode;

    //业务类型
    private String businessCode;

    //签约主体
    private String orgId;

    //借款合同编号
    private String billContractCode;

    //币种
    private String currencyCode;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //年初借方余额
    private BigDecimal yearBeginDebitBalance;

    //年初贷方余额
    private BigDecimal yearBeginCreditBalance;

    //期初借方余额
    private BigDecimal monthBeginDebitBalance;

    //期初贷方余额
    private BigDecimal monthBeginCreditBalance;

    //本期借方发生额
    private BigDecimal monthDebitAmount;

    //本期贷方发生额
    private BigDecimal monthCreditAmount;

    //年累计借方发生额
    private BigDecimal yearDebitAmount;

    //年累计贷方发生额
    private BigDecimal yearCreditAmount;

    //期末借方余额
    private BigDecimal monthEndDebitBalance;

    //期末贷方余额
    private BigDecimal monthEndCreditBalance;

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

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;


}
