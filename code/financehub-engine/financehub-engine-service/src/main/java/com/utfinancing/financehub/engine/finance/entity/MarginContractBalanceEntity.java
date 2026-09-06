package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 保证金合同余额表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_margin_contract_balance")
public class MarginContractBalanceEntity extends Model<MarginContractBalanceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //余额生成日期
    private Date balanceDate;

    //录入日期
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private Date entryDate;

    //业务日期
    private Date businessDate;

    //财务日期
    private Date financeDate;

    //合同编码
    private String contractCode;

    //合同名称
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String contractName;

    //财务账期(yyyyMM)
    private Integer accountPeriod;

    //客户编码
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String clientCode;

    //客户名称
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String clientName;

    //组织机构编码
    private String orgId;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //业务类型编码
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String businessCode;

    //业务类型名称
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String businessName;

    //币种
    @TableField(insertStrategy = FieldStrategy.ALWAYS)
    private String currencyType;

    //合同期末余额
    private BigDecimal contractBalance;

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
//    @TableLogic
    private String delFlag;

    //是否一年内到期(0:否,1:是)
    private String withinOneYear;

    //应付一年内到期保证金
    private BigDecimal withinOneYearDeposit;

    //保证金利息支出
    private BigDecimal depositInterestExpense;

    //保证金利息收入
    private BigDecimal depositInterestIncome;

    //贷款利率
    private BigDecimal lpr;

    //PV
    private BigDecimal pv;

    //本金
    private BigDecimal principalAmount;

    //本期进入PL
    private BigDecimal currentEnterPl;

    //状态(0: 未录入,1: 已录入,2: 已提交,3: 复核通过,4: 复核失败,5: 已传至金蝶)
    private String marginStatus;

    //提交人
    private String submitBy;

    //保证金凭证类型(1-重分类,2-利息计提)
    private String marginType;

    // 批次id
    private Long batchId;

    // 凭证id
    private String voucherId;

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;

    // 异常信息
    private String exceptionType;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

}
