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
 * 应交增值税实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_pay_vat")
public class PayVatEntity extends Model<PayVatEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同id
    private Long contractId;

    //合同编号
    private String contractCode;

    //合同类型
    private String contractType;

    //租赁类型
    private String leaseType;

    //税率
    private BigDecimal taxRate;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //开票标识
    private String invoicingFlag;

    //合同状态
    private String contractStatus;

    //财务合同状态
    private String financialContractStatus;

    //开票主体
    private String orgId;

    //应开税额
    private BigDecimal taxPayable;

    //已计提税额
    private BigDecimal taxAccrued;

    //已开票税额
    private BigDecimal taxInvoiced;

    //实际剩余税额
    private BigDecimal actualTaxBalance;

    //应剩余税额（按计划）
    private BigDecimal shouldTaxBalance;

    //科目余额
    private BigDecimal accountBalance;

    //报表余额
    private BigDecimal reportBalance;

    //异常类型
    private String exceptionType;

    //租赁合同编号
    private String contractCodeM;

    //服务费实收金额
    private BigDecimal serviceFeeReceived;

    //服务费实收日期
    private LocalDateTime serviceFeeReceivedDate;

    //留购价余额
    private BigDecimal retainedPriceBalance;

    //合同起租日
    private LocalDateTime leaseDateStart;

    //合同到期日
    private LocalDateTime leaseDateEnd;

    //处理状态
    private String processStatus;

    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;

    //流程实例id
    private Long processInstanceId;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //记账日期
    private LocalDateTime accountDate;

    //是否删除（0-否，1-是）
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
