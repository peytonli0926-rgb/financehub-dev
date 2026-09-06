package com.utfinancing.financehub.engine.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO</li>
 * <li>CreateTime : 2023/11/29 18:29</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class FieldMappingApiDTO {

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "字段编码")
    private String fieldCode;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;
}
