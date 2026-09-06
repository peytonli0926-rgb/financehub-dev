package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : DTO对象
 * @Modified :
 */
@ApiModel(value = "回收设备财务出库详细表模板导出")
@Data
public class RecyclingEquipmentOutDetailExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编码*",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体*",width = 20)
    private String orgName;

    @ApiModelProperty(value = "出库日期")
    @Excel(name = "出库日期*（yyyy-MM-dd）", dateFormat = "yyyy-MM-dd",width = 30,cellType = Excel.ColumnType.DATE)
    private String outboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
}
