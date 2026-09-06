package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 07/10/2023
 */
@Data
public class SceneRuleConfigSaveDTO {

    @ApiModelProperty(value = "*场景ID")
    @NotNull(message = "场景ID不能为空")
    private Long sceneId;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty("版本")
    private Integer version;

    @ApiModelProperty(value = "规则集合")
    private List<SceneRuleSaveDTO> ruleList;

}
