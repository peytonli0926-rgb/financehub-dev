package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel
public class ConvertTransferContractFeeQueryDTO extends BaseQueryDTO {

    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    @ApiModelProperty("签约主体")
    private List<String> orgIdList;
}
