package com.utfinancing.financehub.engine.integration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Data
public class StaffQueryDTO {

    @ApiModelProperty(name = "员工号")
    private String staffCode;

    @ApiModelProperty(name = "姓名")
    private String staffName;

}
