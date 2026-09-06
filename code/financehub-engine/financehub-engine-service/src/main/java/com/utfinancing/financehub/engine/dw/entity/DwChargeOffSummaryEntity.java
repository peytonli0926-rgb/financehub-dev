package com.utfinancing.financehub.engine.dw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dw_chargeoff_summary")
public class DwChargeOffSummaryEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 签约主体
     */
    private String orgName;

    /**
     * 核销状态
     */
    private String verificationStatus;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 核销时间
     */
    private String verificationDate;

    /**
     * 财务核销敞口
     */
    private BigDecimal financialExpenseAmount;

    /**
     * 10年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount10;

    /**
     * 9年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount9;

    /**
     * 8年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount8;

    /**
     * 7年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount7;

    /**
     * 6年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount6;

    /**
     * 5年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount5;

    /**
     * 4年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount4;

    /**
     * 3年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount3;

    /**
     * 2年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount2;

    /**
     * 1年前拨备转回金额
     */
    private BigDecimal provisionReversalAmount1;

    /**
     * 当前年拨备转回金额
     */
    private BigDecimal provisionReversalAmount0;

    /**
     * 拨备转回金额总计
     */
    private BigDecimal provisionReversalAmountTotal;

    /**
     * 坏账核销余额
     */
    private BigDecimal badDebtWriteOffBalance;

    /**
     * 税务核销日期
     */
    private String taxVerificationDate;

    /**
     * 税务核销金额
     */
    private BigDecimal taxVerificationAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 期间代码
     */
    private Integer periodCode;
}
