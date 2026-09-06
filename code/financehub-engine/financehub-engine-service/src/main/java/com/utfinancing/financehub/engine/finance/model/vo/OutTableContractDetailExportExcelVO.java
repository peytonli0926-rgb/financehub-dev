package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailExportExcelVO</li>
 * <li>CreateTime : 2024/03/13 11:09</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class OutTableContractDetailExportExcelVO {
    @ApiModelProperty(value = "借款合同编码")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "封包日应收租金")
    @Excel(name = "封包日应收租金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "封包日应收残值")
    @Excel(name = "封包日应收残值",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "封包日应收销项税")
    @Excel(name = "封包日应收销项税",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "封包日未实现收益")
    @Excel(name = "封包日未实现收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "封包日承租人保证金")
    @Excel(name = "封包日承租人保证金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "拨备余额")
    @Excel(name = "拨备余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationReservesBalance;

    @ApiModelProperty(value = "封包日后计提收益")
    @Excel(name = "封包日后计提收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal leaseRevenueBalance;

    @ApiModelProperty(value = "封包日后收款")
    @Excel(name = "封包日后收款",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableUnconfirmReceiptAmount;

    @ApiModelProperty(value = "封包日后开票")
    @Excel(name = "封包日后开票",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttaxDebtRestructureAmount;
}
