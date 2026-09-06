package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应交销项税明细实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_receive_tax_detail")
public class ReceiveTaxDetailEntity extends Model<ReceiveTaxDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //单据号
    private String billNo;

    //单据状态
    private String billStatus;

    //发票类型
    private String invoiceType;

    //发票代码
    private String invoiceCode;

    //发票号码
    private String invoiceNumber;

    //开票日期
    private Date invoiceDate;

    //购方名称
    private String purchaserName;

    //购方税号
    private String purchaserTaxCode;

    //购方地址
    private String purchaserDressTel;

    //购方银行账号
    private String purchaserBankNameNum;

    //销方名称
    private String sellerName;

    //销方税号
    private String sellerTaxCode;

    //销方地址电话
    private String sellerDressTel;

    //销方银行账号
    private String sellerBankNameNum;

    //备注（合同编号）
    private String remark;

    //收款人
    private String receiptName;

    //复核人
    private String reviewName;

    //开票人
    private String drawerName;

    //开票机编号
    private String invoiceMachineNo;

    //商品名称
    private String productName;

    //规格型号
    private String model;

    //单位
    private String unit;

    //数量
    private String quantity;

    //金额
    private BigDecimal totalAmount;

    //税额
    private BigDecimal taxAmount;

    //税率
    private BigDecimal taxRate;

    //税收分类编码
    private String taxClassificationCode;

    //税收分类编码名称
    private String taxClassificationName;

    //是否删除（0-否，1-是）
    @TableLogic
    private String delFlag;

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


}
