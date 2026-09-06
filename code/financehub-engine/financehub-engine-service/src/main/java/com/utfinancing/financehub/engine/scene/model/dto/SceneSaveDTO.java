package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 业务场景DTO对象
 * @Modified :
 */
@Data
public class SceneSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "场景周期")
    private String scenePeriod;

    @ApiModelProperty(value = "是否有效(0:无效,1:有效)")
    private String enableFlag;

}
