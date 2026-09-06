package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 14/09/2023
 */
@Data
public class EditorOptionItemDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "子选项类型")
    private String type;

    @ApiModelProperty(value = "子选项名称")
    private String name;

    @ApiModelProperty(value = "子选项编码")
    private String code;

    @ApiModelProperty(value = "映射后编码")
    private Object value;

    @ApiModelProperty(value = "说明")
    private String desc;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

}
