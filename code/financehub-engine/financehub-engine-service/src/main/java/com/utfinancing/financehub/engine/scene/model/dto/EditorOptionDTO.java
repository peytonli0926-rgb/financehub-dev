package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 14/09/2023
 */
@Data
public class EditorOptionDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "选项名称")
    private String name;

    @ApiModelProperty(value = "子选项列表")
    private List<EditorOptionItemDTO> children;

}
