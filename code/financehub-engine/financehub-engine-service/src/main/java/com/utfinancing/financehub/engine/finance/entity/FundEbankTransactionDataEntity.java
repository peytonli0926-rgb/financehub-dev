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
 * 网银收付款数据表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_fund_ebank_transaction_data")
public class FundEbankTransactionDataEntity extends Model<FundEbankTransactionDataEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //交易类型(必填,收款：collection,付款：payment)
    private String transactionType;

    //交易流水号(必填，需保证唯一)
    private String orderId;

    //业务日期(yyyy-MM-dd HH:mm:ss)
    private String businessDate;

    //操作日期(yyyy-MM-dd HH:mm:ss)
    private String operationDate;

    //业务事件
    private String businessOperation;

    //网银编号
    private String ebankNumber;

    //收款账号（虚拟户）
    private String collectionAccountsBankNo;

    //收款开户行
    private String collectionAccountsBank;

    //对方合同号
    private String contractCode;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //网银金额
    private BigDecimal bankAmount;

    //备注
    private String comment;

    //对方客户开户行
    private String clientAccountsBank;

    //对方客户银行账号
    private String clientAccountsBankNo;

    //签约主体
    private String orgId;
    //应收票据金额
    private BigDecimal receivableBill;
    //应付票据金额
    private BigDecimal payableBill;
    //付款账号
    private String paymentAccountsBankNo;

    //币种
    private String currencyType;

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

    //是否生成凭证
    private String isGenerateVoucher;

    //银行交易摘要
    private String bankSummary;

    //收款类型
    private String collectionType;

    private String billNumber;
    //银行签约主体
    private String bankOrgId;
}
