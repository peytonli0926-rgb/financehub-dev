package com.utfinancing.financehub.engine.integration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Data
public class OrgQueryDTO {

    @ApiModelProperty(name = "部门编码")
    private String orgCode;

    @ApiModelProperty(name = "部门名称")
    private String orgName;

    @ApiModelProperty(name = "子部门层级-1：全部层级0: 当前层级")
    private Integer childFloor;

}
