package com.utfinancing.financehub.engine.integration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@NoArgsConstructor
@Data
public class PositionDTO {

    @ApiModelProperty(name = "机构编码")
    private String positionBelongOrgCode;

    @ApiModelProperty(name = "机构名称")
    private String positionBelongOrgName;

    @ApiModelProperty(name = "岗位编码")
    private String positionCode;

    @ApiModelProperty(name = "岗位代码")
    private String positionAuthCode;

    @ApiModelProperty(name = "岗位名称")
    private String positionName;

    @ApiModelProperty(name = "岗位级别编码")
    private String positionGrade;

    @ApiModelProperty(name = "岗位级别名称")
    private String positionGradeName;

    @ApiModelProperty(name = "岗位属性")
    private String positionProperty;

    @ApiModelProperty(name = "岗位属性名称")
    private String positionPropertyName;

    @ApiModelProperty(name = "是否禁用")
    private Integer disabled;

    @ApiModelProperty(name = "是否内部岗位")
    private Integer isInnerFlag;

}
