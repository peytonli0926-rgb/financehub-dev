package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferOtherPlanVO {
    @Excel(name = "合同编号")
    @ApiModelProperty("合同编号")
    private String contractCode;

    @Excel(name = "日期")
    @ApiModelProperty("日期")
    private String planDate;

    @Excel(name = "期数")
    @ApiModelProperty("期数")
    private Integer periods;

    @Excel(name = "应收租金")
    @ApiModelProperty("应收租金")
    private BigDecimal rentAmount;

    @Excel(name = "应收本金")
    @ApiModelProperty("应收本金")
    private BigDecimal principalAmount;

    @Excel(name = "应收利息")
    @ApiModelProperty("应收利息")
    private BigDecimal interestAmount;

    @Excel(name = "实收日期", dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("实收日期")
    private LocalDate actualRepaymentDate;

    @Excel(name = "实收租金")
    @ApiModelProperty("实收租金")
    private BigDecimal actualAmount;

    @Excel(name = "实收本金")
    @ApiModelProperty("实收本金")
    private BigDecimal actualRepaymentAmount;

    @Excel(name = "实收利息")
    @ApiModelProperty("实收利息")
    private BigDecimal actualRepaymentInteresAmount;

    @Excel(name = "实际开票")
    @ApiModelProperty("实际开票")
    private BigDecimal taxValue;

}
