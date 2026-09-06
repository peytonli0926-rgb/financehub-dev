package com.utfinancing.financehub.engine.claim.entity;

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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 报销系统-报销单发票明细实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_claim_order_invoice")
public class ClaimOrderInvoiceEntity extends Model<ClaimOrderInvoiceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //报销单主表ID
    private Long claimOrderId;

    //单据编号
    private String orderNo;

    //发票号码
    private String invoiceNumber;

    //发票类型
    private String invoiceType;

    //购买机构
    private String purchaseOrganization;

    //销售方
    private String seller;

    //开票日期
    private LocalDateTime invoiceDate;

    //税价合计
    private String totalAmount;

    //无税金额
    private BigDecimal noTaxAmount;

    //税率
    private BigDecimal taxRate;

    //税额
    private BigDecimal taxAmount;

    //发票内容
    private String invoiceContent;

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

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;


}
