package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO</li>
 * <li>CreateTime : 2024/04/03 10:11</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class CheckPageQueryDTO extends BaseQueryDTO {

    @ApiModelProperty("id")
    @NotNull(message = "id不可以为空")
    private Long id;
}
