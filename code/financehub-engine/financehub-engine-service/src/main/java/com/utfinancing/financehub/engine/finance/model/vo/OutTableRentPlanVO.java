package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.OutTableRentPlanVO</li>
 * <li>CreateTime : 2024/03/20 10:33</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("出表合同租金计划VO")
@Data
public class OutTableRentPlanVO {

    @ApiModelProperty("日期")
    @Excel(name = "日期",width = 20)
    private String planDate;

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "期数")
    @Excel(name = "期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20)
    private String rate;

    @ApiModelProperty(value = "应收本金")
    @Excel(name = "应收本金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "应收利息")
    @Excel(name = "应收利息",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "实收本金")
    @Excel(name = "实收本金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRepaymentPrincipalAmount;

    @ApiModelProperty(value = "实收利息")
    @Excel(name = "实收利息",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualRepaymentInteresAmount;

    @ApiModelProperty(value = "实收留够价")
    @Excel(name = "实收留够价",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValueAmount;

    @ApiModelProperty(value = "实收罚息及手续费")
    @Excel(name = "实收罚息及手续费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableTerminateAmount;
}
