package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ContractRepaymentPlanVO</li>
 * <li>CreateTime : 2024/01/04 09:40</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("回笼计划")
@Data
public class ContractRepaymentPlanVO {

    @ApiModelProperty(value = "计划期数")
    @Excel(name = "计划期数",width = 20)
    private Integer periods;

    @ApiModelProperty(value = "计划还款日")
    @Excel(name = "计划还款日",width = 20)
    private String planDatePeriod;

    @ApiModelProperty(value = "计划租金")
    @Excel(name = "计划租金",width = 20)
    private BigDecimal rentAmount;

    @ApiModelProperty(value = "计划本金")
    @Excel(name = "计划本金",width = 20)
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "计划利息")
    @Excel(name = "计划利息",width = 20)
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "实收日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "实收日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date actualRepaymentDate;

    @ApiModelProperty(value = "实收租金")
    @Excel(name = "实收租金",width = 20)
    private BigDecimal actualRepaymentRentAmount;

    @ApiModelProperty(value = "实收利息")
    @Excel(name = "实收利息",width = 20)
    private BigDecimal actualRepaymentInteresAmount;

    @ApiModelProperty(value = "实收本金")
    @Excel(name = "实收本金",width = 20)
    private BigDecimal actualRepaymentPrincipalAmount;

    @ApiModelProperty(value = "网银编号")
    @Excel(name = "网银编号",width = 20)
    private String bankNum;

    @ApiModelProperty(value = "结算方式")
    @Excel(name = "结算方式",width = 20)
    private String settlementTerms;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "调整金额")
    private BigDecimal adjustmentAmount;
    @ApiModelProperty(value = "期末摊余成本差额")
    private BigDecimal endingAmortizedCostBalance;
    @ApiModelProperty(value = "租赁收入差额")
    private BigDecimal rentalIncomeBalance;
    @ApiModelProperty(value = "TA发生额")
    private BigDecimal taBalance;
}
