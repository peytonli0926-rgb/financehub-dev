package com.utfinancing.financehub.engine.rule.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 */
@ApiModel("MqErrorMessageRePushReqDTO")
@Data
public class MqErrorMessageRePushReqDTO{


    @ApiModelProperty(value = "ID列表")
    private List<Long> ids;

}
