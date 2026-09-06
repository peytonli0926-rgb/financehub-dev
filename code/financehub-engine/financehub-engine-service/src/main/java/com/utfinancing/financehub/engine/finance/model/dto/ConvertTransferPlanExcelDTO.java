package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-租金计划DTO对象
 * @Modified :
 */
@Data
public class ConvertTransferPlanExcelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "原合同编码")
    @Excel(name = "原合同编码")
    private String oldContractCode;

    @ApiModelProperty(value = "新合同编码")
    @Excel(name = "新合同编码")
    private String newContractCode;

    @ApiModelProperty(value = "计划日期")
    @Excel(name = "计划日期", dateFormat = "yyyy-MM-dd")
    private Date planDate;

    @ApiModelProperty(value = "期数")
    @Excel(name = "期数")
    private Integer periods;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收本金")
    @Excel(name = "应收本金")
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    @Excel(name = "应收利息")
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "应收期末残值")
    @Excel(name = "应收期末残值")
    private BigDecimal receivableEndingSalvage;

}
