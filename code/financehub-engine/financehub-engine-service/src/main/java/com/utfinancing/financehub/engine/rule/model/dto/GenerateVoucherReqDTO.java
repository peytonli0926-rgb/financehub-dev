package com.utfinancing.financehub.engine.rule.model.dto;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 26/10/2023
 */
@Data
public class GenerateVoucherReqDTO {

    @ApiModelProperty("交易参数")
    private JSONObject param;

}
