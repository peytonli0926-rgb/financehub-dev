package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContractRepaymentPlan查询from对象
 * @Modified :
 */
@ApiModel("OfflineContractRepaymentPlan查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineContractRepaymentPlanQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "期数")
    private Integer period;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
}
