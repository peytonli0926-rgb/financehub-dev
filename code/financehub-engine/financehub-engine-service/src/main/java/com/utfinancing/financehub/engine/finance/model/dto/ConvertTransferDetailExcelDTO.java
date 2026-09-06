package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-详情DTO对象
 * @Modified :
 */
@Data
public class ConvertTransferDetailExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "转让批次")
    @Excel(name = "转让批次")
    private String batch;

    @ApiModelProperty(value = "原合同编码")
    @Excel(name = "原合同编码")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    @Excel(name = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收销项税")
    @Excel(name = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现融资租赁收益")
    @Excel(name = "未实现融资租赁收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "承租人保证金")
    @Excel(name = "承租人保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    @Excel(name = "应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "评估价")
    @Excel(name = "评估价")
    private BigDecimal appraisedValue;

    @ApiModelProperty(value = "转让时敞口")
    @Excel(name = "转让时敞口")
    private BigDecimal transferOpen;

    @ApiModelProperty(value = "补提拨备")
    @Excel(name = "补提拨备")
    private BigDecimal supplementaryProvision;

    @ApiModelProperty(value = "收益确认")
    @Excel(name = "收益确认")
    private BigDecimal revenueRecognition;

}
