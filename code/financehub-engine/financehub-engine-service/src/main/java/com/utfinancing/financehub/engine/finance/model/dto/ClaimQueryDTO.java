package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClaimQueryDTO implements Serializable {
    @ApiModelProperty(value = "未确认收款ID")
    private Long id;
}
