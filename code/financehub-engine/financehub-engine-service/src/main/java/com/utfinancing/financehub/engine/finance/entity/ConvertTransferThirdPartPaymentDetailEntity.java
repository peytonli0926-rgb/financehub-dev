package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 资产转让-折价转让-内部调拨实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer_third_payment_detail")
public class ConvertTransferThirdPartPaymentDetailEntity extends Model<ConvertTransferThirdPartPaymentDetailEntity> {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 批次
     */
    private String batch;


    private Long paymentId;


    /**
     * 转让方
     */
    private String transferParty;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 客户code
     */
    private String clientCode;

    /**
     * 客户名称
     */
    private String clientName;
    /**
     * 受让方
     */
    private String orgId;

    /**
     * 支付主体
     */
    private String paymentOrgId;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 业务日期
     */
    private LocalDate tradeDate;

    /**
     * 记账日期
     */
    private LocalDate accountDate;

    /**
     * 支付日期
     */
    private LocalDate paymentDate;

    /**
     * 银行账号编码
     */
    private String bankAccountCode;

    /**
     * 凭证id,多个按照逗号分隔
     */
    private String voucherId;

    /**
     * 生成凭证报错信息
     */
    private String errorInfo;

    /**
     * 是否删除（0-否，1-是）
     */
    @TableLogic
    private String delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
