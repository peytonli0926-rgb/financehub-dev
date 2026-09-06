package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClaimQueryResultDTO implements Serializable {

    @ApiModelProperty(value = "未确认收款ID")
    private Long id;

    @ApiModelProperty(value = "认领数据明细")
    private List<ClaimQueryResultDetailDTO> claimQueryResultDetailDTO = new ArrayList<>();

//    @ApiModelProperty(value = "认领主体列表")
//    private List<ContractDTO> contractDTOList = new ArrayList<>();
}
