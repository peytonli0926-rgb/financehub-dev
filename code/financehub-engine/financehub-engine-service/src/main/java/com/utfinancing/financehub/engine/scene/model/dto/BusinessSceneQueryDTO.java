package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :   BusinessScene查询from对象
 * @Modified :
 */
@ApiModel("BusinessScene查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessSceneQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务配置ID")
    private Long businessId;

    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "序号")
    private Integer serial;
}
