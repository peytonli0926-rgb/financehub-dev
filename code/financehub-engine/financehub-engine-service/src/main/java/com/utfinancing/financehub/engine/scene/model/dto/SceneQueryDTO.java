package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   Scene查询from对象
 * @Modified :
 */
@ApiModel("Scene查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SceneQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "场景周期")
    private String scenePeriod;

    @ApiModelProperty(value = "是否有效(0:无效,1:有效)")
    private String enableFlag;
}
