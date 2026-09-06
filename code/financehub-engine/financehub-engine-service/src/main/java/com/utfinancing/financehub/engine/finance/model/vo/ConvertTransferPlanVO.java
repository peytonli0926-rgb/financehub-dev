package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-租金计划VO对象
 * @Modified :
 */
@Data
public class ConvertTransferPlanVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "原合同编码")
    @ApiModelProperty(value = "原合同编码")
    private String oldContractCode;

    @Excel(name = "新合同编码")
    @ApiModelProperty(value = "新合同编码")
    private String newContractCode;

    @Excel(name = "计划日期", dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期")
    private LocalDateTime planDate;

    @Excel(name = "期数")
    @ApiModelProperty(value = "期数")
    private Integer periods;

    @Excel(name = "应收租金")
    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @Excel(name = "应收本金")
    @ApiModelProperty(value = "应收本金")
    private BigDecimal receivablePrincipal;

    @Excel(name = "应收利息")
    @ApiModelProperty(value = "应收利息")
    private BigDecimal receivableInterest;

    @Excel(name = "应收期末残值")
    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableEndingSalvage;

}
