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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 偿还计划测算历史表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_repayment_plan_his")
public class RepaymentPlanHisEntity extends Model<RepaymentPlanHisEntity> {

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
    private Integer periods;

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

    //实际归还日期
    private Date actualRepaymentDate;

    //实际归还本金余额
    private BigDecimal actualRepaymentPrincipalBalance;

    //实际归还本金发生额
    private BigDecimal actualRepaymentPrincipalAmount;

    //实际归还利息余额
    private BigDecimal actualRepaymentInteresBalance;

    //实际归还利息发生额
    private BigDecimal actualRepaymentInteresAmount;

    //回笼状态
    private String recaptureStatus;

    //逾期收益
    private BigDecimal overdueEarnings;

    //备注
    private String comment;

    //逾期天数
    private Integer overdueDays;

    //处理状态
    private String processStatus;

    //提交人
    private String submitBy;

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

    private String systemCode;
    private String orgId;

    private String messageId;
    //还款标识 期初(下还),期末(上还)
    private String payMethod;

    // 变更日期
    private Date changeDate;

    //交易结构手工调整标志
    private String manualChangeMark;

    // 手工逾期标识
    private String laborOverdueMark;

    private Integer version;

    private BigDecimal sourceIrrRate;

    @ApiModelProperty("网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("结算方式")
    private String settlementWay;

    @ApiModelProperty("未实现收益总额")
    private BigDecimal unrealizedRevenue;


    // 调整金额
    private BigDecimal adjustmentAmount;

    // 变更后期末摊余成本
    private BigDecimal changeAfterEndingAmortizedCost;

    // 变更后租赁收入
    private BigDecimal changeAfterRentalIncome;
}
