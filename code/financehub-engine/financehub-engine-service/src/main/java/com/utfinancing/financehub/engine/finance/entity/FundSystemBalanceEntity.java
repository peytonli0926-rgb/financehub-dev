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
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资金系统余额表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_fund_system_balance")
public class FundSystemBalanceEntity extends Model<FundSystemBalanceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //银行账号
    private String bankNo;

    //应收票据余额
    private String receivableBillBalance;

    //应收票据发生额
    private String receivableBillAmount;

    //凭证id
    private Long voucherId;

    //交易流水号
    private String orderId;

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

    //是否删除（0：否，1：是）
    @TableLogic
    private String delFlag;

    //凭证日期
    private LocalDateTime voucherDate;

    //应付票据余额
    private BigDecimal payableBillBalance;

    //应付票据发生额
    private BigDecimal payableBillAmount;

    //应付未付款余额
    private BigDecimal payableAccountBalance;

    //应付未付款发生额
    private BigDecimal payableAccountAmount;

    //未确认收款余额
    private BigDecimal receivableUnconfirmReceiptBalance;

    //未确认收款发生额
    private BigDecimal receivableUnconfirmReceiptAmount;

    //银行账户余额
    private BigDecimal bankDepositsBalance;

    //银行账户发生额
    private BigDecimal bankDepositsAmount;

    //资金交易类型
    private String transactionType;


}
