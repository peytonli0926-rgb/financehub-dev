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
@ApiModel(value = "回收设备财务入库汇总表模板导出")
@Data
public class RecyclingEquipmentInExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "上传批次")
    @Excel(name = "上传批次",width = 20)
    private String batchNumber;

    @ApiModelProperty(value = "入库日期")
    @Excel(name = "入库日期*（yyyy-MM-dd）", dateFormat = "yyyy-MM-dd",width = 30,cellType = Excel.ColumnType.DATE)
    private String inboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体*",width = 20)
    private String orgName;

    @ApiModelProperty(value = "财务敞口")
    @Excel(name = "财务敞口",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExposure;

    @ApiModelProperty(value = "回收设备成本")
    @Excel(name = "回收设备成本",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "入库时计提减值")
    @Excel(name = "入库时计提减值",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "处理状态代码")
    private String processStatus;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态",width = 20)
    private String processStatusDesc;
}
