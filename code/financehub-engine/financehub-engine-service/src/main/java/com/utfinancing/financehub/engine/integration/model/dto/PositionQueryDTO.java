package com.utfinancing.financehub.engine.integration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Data
public class PositionQueryDTO {

    @ApiModelProperty(name = "岗位编码")
    private String positionCode;

    @ApiModelProperty(name = "岗位名称")
    private String positionName;

}
