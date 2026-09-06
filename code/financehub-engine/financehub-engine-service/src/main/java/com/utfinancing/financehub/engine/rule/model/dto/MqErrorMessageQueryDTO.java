package com.utfinancing.financehub.engine.rule.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 * @Description :   MqErrorMessage查询from对象
 * @Modified :
 */
@ApiModel("MqErrorMessage查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class MqErrorMessageQueryDTO extends BaseQueryDTO{


    @ApiModelProperty(value = "消息处理状态 0:未处理 1:已重推  2:已忽略")
    private String status;

    @ApiModelProperty(value = "消息内容")
    private String exceptionMessage;

    @ApiModelProperty(value = "原始路由key")
    private String originalRoutingKey;

}
