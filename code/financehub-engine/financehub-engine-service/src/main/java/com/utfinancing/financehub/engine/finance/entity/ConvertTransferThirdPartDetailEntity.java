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
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer_third_detail")
public class ConvertTransferThirdPartDetailEntity extends Model<ConvertTransferThirdPartDetailEntity> {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 折价转让id
     */
    private Long transferId;

    /**
     * 转让批次
     */
    private String batch;

    /**
     * 原合同编码
     */
    private String contractCode;

    /**
     * 客户编码
     */
    private String clientCode;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 签约主体
     */
    private String orgId;

    /**
     * 财务合同状态
     */
    private String financialContractStatus;


    /**
     * 应收租金
     */
    private BigDecimal receivableRent;

    /**
     * 应收期末残值
     */
    private BigDecimal receivableResidualValue;

    /**
     * 应收销项税
     */
    private BigDecimal receivableOuttax;

    /**
     * 未实现融资租赁收益
     */
    private BigDecimal unrealizedRevenue;

    /**
     * 承租人保证金
     */
    private BigDecimal lesseeMargin;

    /**
     * 应收租赁款组合拨备
     */
    private BigDecimal depreciationReserves;

    /**
     * 评估价
     */
    private BigDecimal appraisedValue;

    /**
     * 转让时敞口
     */
    private BigDecimal transferOpen;

    /**
     * 补提拨备
     */
    private BigDecimal supplementaryProvision;


    /**
     * 基准日后计提收益
     * 场景为SYJT，lease_revenue6_amount+lease_revenue_amount
     */
    private BigDecimal baseDateAccruedIncome;

    /**
     * 基准日后计提拨备
     * 场景为JZJT，depreciation_reserves_amount汇总
     */
    private BigDecimal baseDateProvision;

    /**
     * 基准日后收款
     * 场景为ZLSK，receivable_unconfirm_receipt_amount的汇总金额
     */
    private BigDecimal baseDateReceive;

    /**
     * 基准日后开票
     *
     * 场景为KJFP，receivable_service_outtax_amount + receivable_outtax_amount + 特殊状态使用科目的汇总金额
     */
    private BigDecimal baseDateInvoices;

    /**
     * 凭证id,多个按照逗号分隔
     */
    private String voucherId;

    /**
     * 生成凭证报错信息
     */
    private String errorInfo;

    /**
     * 记账日期
     */
    private LocalDateTime accountDate;

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
