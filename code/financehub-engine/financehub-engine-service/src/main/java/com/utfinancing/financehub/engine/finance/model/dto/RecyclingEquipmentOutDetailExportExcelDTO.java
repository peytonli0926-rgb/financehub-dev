package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : VO对象
 * @Modified :
 */
@Data
public class RecyclingEquipmentOutDetailExportExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体名称",width = 20)
    private String orgName;

    @ApiModelProperty(value = "出库日期")
    @Excel(name = "出库日期", dateFormat = "yyyy-MM-dd",width = 30,cellType = Excel.ColumnType.DATE)
    private String outboundDate;

    @ApiModelProperty(value = "回收设备成本")
    @Excel(name = "回收设备成本",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "回收设备减值")
    @Excel(name = "回收设备减值",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;


    @ApiModelProperty(value = "处理状态描述")
    @Excel(name = "处理状态",width = 20)
    private String processStatusDesc;
}
