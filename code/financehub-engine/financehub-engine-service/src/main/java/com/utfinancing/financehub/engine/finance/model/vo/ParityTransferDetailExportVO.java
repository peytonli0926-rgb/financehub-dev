package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailExportVO</li>
 * <li>CreateTime : 2024/04/02 17:15</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
@ApiModel("平价转让详情导出VO")
public class ParityTransferDetailExportVO {

    @ApiModelProperty(value = "转让批次")
    @Excel(name = "转让批次",width = 20)
    private String batch;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编码",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgId;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20)
    private String taxRate;

    @ApiModelProperty(value = "应收租金余额")
    @Excel(name = "应收租金",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值余额")
    @Excel(name = "应收期末残值",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收销项税余额")
    @Excel(name = "应收销项税",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益余额")
    @Excel(name = "未实现收益",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "承租人保证金余额")
    @Excel(name = "承租人保证金",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "应付经销商服务费-暂估余额")
    @Excel(name = "应付经销商服务费-暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估余额")
    @Excel(name = "应付收车费-暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估余额")
    @Excel(name = "应付手环成本_暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估余额")
    @Excel(name = "应付抵押费_暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估余额")
    @Excel(name = "应付解抵押费_暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估余额")
    @Excel(name = "应付其他租赁成本-暂估",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "减值准备余额-应收租赁款组合拨备")
    @Excel(name = "应收租赁款组合拨备",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "评估价")
    @Excel(name = "评估价",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal appraisedValue;
}
