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
@ApiModel(value = "批量查询模板导出")
@Data
public class BatchQueryUploadExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码",width = 20)
    private String accountCode;

    @ApiModelProperty(value = "合同编码")
    @Excel(name = "合同编码*",width = 20)
    private String contractCode;

}
