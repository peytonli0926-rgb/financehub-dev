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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 线下合同租金计划实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_offline_contract_repayment_plan")
public class OfflineContractRepaymentPlanEntity extends Model<OfflineContractRepaymentPlanEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //期数
    private Integer period;

    //日期
    private Date planDate;

    //租金
    private BigDecimal rentAmount;

    //本金
    private BigDecimal principalAmount;

    //利息
    private BigDecimal interestAmount;

    //本金-税金
    private BigDecimal principalTax;

    //利息-税金
    private BigDecimal interestTax;

    //资金流出
    private BigDecimal outflowAmount;

    //计划利息(不含税)
    private BigDecimal plannedInterest;

    //计划本金(不含税)
    private BigDecimal plannedPrincipal;

    //现金流
    private BigDecimal cashFlow;

    //期初摊余成本
    private BigDecimal openingAmortizedCost;

    //期末摊余成本
    private BigDecimal endingAmortizedCost;

    //实际日利率
    private BigDecimal actualDailyRate;

    //租赁收入
    private BigDecimal rentalIncome;

    //服务费摊销利率
    private BigDecimal serviceFeeAmortizationRate;

    //服务费摊销收入
    private BigDecimal serviceFeeAmortizationIncome;

    //XIRR
    private BigDecimal xirrRate;

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
