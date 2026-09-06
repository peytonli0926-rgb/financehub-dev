package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("eg_convert_transfer_other_payment")
public class ConvertTransferOtherPaymentEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 客户编码
     */
    private String clientCode;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 实付日期
     */
    private LocalDate actualDate;

    /**
     * 记账日期
     */
    private LocalDate accountDate;

    /**
     * 计划日期
     */
    private LocalDate planDate;

    /**
     * 计划期数
     */
    private Integer period;

    /**
     * 应付租金
     */
    private BigDecimal rentAmount;

    /**
     * 应付本金
     */
    private BigDecimal principalAmount;

    /**
     * 应付利息
     */
    private BigDecimal interestAmount;

    /**
     * 实付租金
     */
    private BigDecimal rentActual;

    /**
     * 实付本金
     */
    private BigDecimal principalActual;

    /**
     * 实付利息
     */
    private BigDecimal interestActual;

    /**
     * 计算扣额
     */
    private BigDecimal calculateDeductions;

    /**
     * 转让后是否开发票（0：否，1：是）
     */
    private String invoiceFlag;

    /**
     * 银行账户编码
     */
    private String bankAccountCode;

    /**
     * 流程id
     */
    private Long processInstanceId;

    /**
     * 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
     */
    private String processStatus;

    /**
     * 是否已生成凭证（0：未生成1：已生成）默认0
     */
    private String isGenerateVoucher;

    /**
     * 凭证id,多个按照逗号分隔
     */
    private String voucherId;

    /**
     * 生成凭证报错信息
     */
    private String errorInfo;

    //是否删除（0：未删除1：删除）默认0
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
