package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 业务场景接口字段DTO对象
 * @Modified :
 */
@Data
public class SceneFieldsTreeChildrenDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "字段类型")
    private String type;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

}
