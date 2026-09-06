package com.utfinancing.financehub.engine.claim.entity;

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
 * 报销系统-报销单付款信息表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_claim_order_payment")
public class ClaimOrderPaymentEntity extends Model<ClaimOrderPaymentEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //报销单主表ID
    private Long claimOrderId;

    //单据编号
    private String orderNo;

    //收款人名称
    private String payeeName;

    //收款账户
    private String receiptAccount;

    //收款支行
    private String receiptBranchBank;

    //收款行联行号
    private String receiptBankUnionNum;

    //支付方式
    private String paymentWay;

    //支付金额
    private String paymentAmount;

    //swiftCode
    private String swiftCode;

    //银行名称
    private String bankName;

    //银行编码（名称）
    private String bankNum;

    //交易附言
    private String dealAddition;

    //市
    private String city;

    //省
    private String province;

    //收款行国家/地区
    private String receiptCountry;

    //收款人常驻国家/地区
    private String payeeCountry;

    //境外收款行地址
    private String receiptBankOverseasAddress;

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
