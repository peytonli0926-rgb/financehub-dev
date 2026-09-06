package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : ta其他应付款汇总VO对象
 * @Modified :
 */
@Data
public class TaOtherPayableExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "重分类金额")
    @Excel(name = "重分类金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal reclassificationAmount;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;

}
