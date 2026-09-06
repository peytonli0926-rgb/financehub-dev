package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description : 偿还计划测算历史表VO对象
 * @Modified :
 */
@Data
public class RepaymentPlanHisVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "期数（计划期数）")
    private Integer periods;

    @ApiModelProperty(value = "日期")
    private Date planDate;

    @ApiModelProperty(value = "租金")
    private BigDecimal rentAmount;

    @ApiModelProperty(value = "本金")
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "利息")
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "本金-税金")
    private BigDecimal principalTax;

    @ApiModelProperty(value = "利息-税金")
    private BigDecimal interestTax;

    @ApiModelProperty(value = "资金流出")
    private BigDecimal outflowAmount;

    @ApiModelProperty(value = "计划利息(不含税)")
    private BigDecimal plannedInterest;

    @ApiModelProperty(value = "计划本金(不含税)")
    private BigDecimal plannedPrincipal;

    @ApiModelProperty(value = "现金流")
    private BigDecimal cashFlow;

    @ApiModelProperty(value = "期初摊余成本")
    private BigDecimal openingAmortizedCost;

    @ApiModelProperty(value = "期末摊余成本")
    private BigDecimal endingAmortizedCost;

    @ApiModelProperty(value = "实际日利率")
    private BigDecimal actualDailyRate;

    @ApiModelProperty(value = "租赁收入")
    private BigDecimal rentalIncome;

    @ApiModelProperty(value = "服务费摊销利率")
    private BigDecimal serviceFeeAmortizationRate;

    @ApiModelProperty(value = "服务费摊销收入")
    private BigDecimal serviceFeeAmortizationIncome;

    @ApiModelProperty(value = "XIRR")
    private BigDecimal xirrRate;

    @ApiModelProperty(value = "实际归还日期")
    private Date actualRepaymentDate;

    @ApiModelProperty(value = "实际归还本金余额")
    private BigDecimal actualRepaymentPrincipalBalance;

    @ApiModelProperty(value = "实际归还本金发生额")
    private BigDecimal actualRepaymentPrincipalAmount;

    @ApiModelProperty(value = "实际归还利息余额")
    private BigDecimal actualRepaymentInteresBalance;

    @ApiModelProperty(value = "实际归还利息发生额")
    private BigDecimal actualRepaymentInteresAmount;

    @ApiModelProperty(value = "回笼状态")
    private String recaptureStatus;

    @ApiModelProperty(value = "逾期收益")
    private BigDecimal overdueEarnings;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "提交人")
    private String submitBy;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty("网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("结算方式")
    private String settlementWay;

    @ApiModelProperty("未实现收益总额")
    private BigDecimal unrealizedRevenue;

}
