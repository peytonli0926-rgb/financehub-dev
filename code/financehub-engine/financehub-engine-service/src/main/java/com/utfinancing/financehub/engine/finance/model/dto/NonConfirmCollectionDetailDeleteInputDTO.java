package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class NonConfirmCollectionDetailDeleteInputDTO implements Serializable {

    @ApiModelProperty(value = "明细ID", required = true)
    private List<String> detailIds = new ArrayList<>();
}
