package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-16
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class EasExchangeRateDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

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

    @ApiModelProperty(value = "")
    private String createBy;

    @ApiModelProperty(value = "")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    private String updateBy;

    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    private String delFlag;

}
