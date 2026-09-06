package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : DTO对象
 * @Modified :
 */
@ApiModel(value = "回收设备财务入库详细表模板导出")
@Data
public class RecyclingEquipmentInDetailExportExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编码*",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户代码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称*",width = 20)
    private String clientName;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体*",width = 20)
    private String orgName;


    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态*",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "入库日期")
    @Excel(name = "入库日期*（yyyy-MM-dd）", dateFormat = "yyyy-MM-dd",width = 30,cellType = Excel.ColumnType.DATE)
    private String inboundDate;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收期末残值")
    @Excel(name = "应收期末残值",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收销项税")
    @Excel(name = "应收销项税",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "未实现收益")
    @Excel(name = "未实现收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "承租人保证金")
    @Excel(name = "承租人保证金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "财务敞口")
    @Excel(name = "财务敞口",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExposure;

    @ApiModelProperty(value = "回收设备成本")
    @Excel(name = "回收设备成本",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "入库时计提减值")
    @Excel(name = "入库时计提减值",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
}
