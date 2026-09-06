package com.utfinancing.financehub.etl.financial.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataInitDTO implements Serializable {
    @ApiModelProperty(value = "合同编码列表")
    private String contractCode;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;
}
