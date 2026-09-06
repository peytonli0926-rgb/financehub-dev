package com.utfinancing.financehub.engine.rule.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 26/10/2023
 */
@Data
public class ValidateSyntaxReqDTO {

    @ApiModelProperty("表达式字符串")
    private String script;

}
