package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 业务场景接口字段DTO对象
 * @Modified :
 */
@Data
public class SceneFieldsSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段编码")
    private String fieldCode;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "父字段ID，List明细字段必填")
    private Long parentId;

    @ApiModelProperty(value = "是否必填(0:否 1:是)")
    private String requiredFlag;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

}
