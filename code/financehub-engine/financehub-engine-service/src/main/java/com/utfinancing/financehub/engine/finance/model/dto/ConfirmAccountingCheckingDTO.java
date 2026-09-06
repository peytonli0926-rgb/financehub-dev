package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmAccountingCheckingDTO implements Serializable {
    @ApiModelProperty(value = "对账月份(yyyy-MM)", required = true)
    private String checkingMonth;
}
