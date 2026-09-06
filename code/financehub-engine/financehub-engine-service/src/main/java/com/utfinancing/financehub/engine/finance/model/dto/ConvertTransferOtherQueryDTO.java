package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class ConvertTransferOtherQueryDTO extends BaseQueryDTO {

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("转让方")
    List<String> transferPartyList;

    @ApiModelProperty("受让方")
    List<String> transfereePartyList;
}
