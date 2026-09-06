package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-16
 * @Description :   EasExchangeRate查询from对象
 * @Modified :
 */
@ApiModel("EasExchangeRate查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class EasExchangeRateQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String executeDate;

    @ApiModelProperty(value = "")
    private String sourceEasCode;

    @ApiModelProperty(value = "")
    private String sourceEasName;

    @ApiModelProperty(value = "")
    private String targetEasCode;

    @ApiModelProperty(value = "")
    private String targetEasName;

    @ApiModelProperty(value = "")
    private BigDecimal exchangeRate;
}
