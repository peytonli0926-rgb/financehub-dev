package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class ConvertTransferContractFeeDetailQueryDTO extends BaseQueryDTO {
    @ApiModelProperty("合同转让费用汇总id")
    private Long transferId;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("费用类型")
    private String transferFeeType;
}
