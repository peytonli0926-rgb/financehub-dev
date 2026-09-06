package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 线下合同租金计划DTO对象
 * @Modified :
 */
@Data
public class OfflineContractRepaymentPlanDTO implements Serializable{
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

}
