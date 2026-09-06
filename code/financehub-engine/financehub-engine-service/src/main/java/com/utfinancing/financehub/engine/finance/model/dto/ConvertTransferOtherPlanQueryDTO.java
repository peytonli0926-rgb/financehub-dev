package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class ConvertTransferOtherPlanQueryDTO extends BaseQueryDTO {

    @ApiModelProperty("合同号")
    private List<String> contractCodes;

    @ApiModelProperty("开始日期")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    private LocalDate endDate;
}
