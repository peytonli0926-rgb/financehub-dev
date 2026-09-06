package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description : 邮储手续费详情DTO对象
 * @Modified :
 */
@Data
public class PostalStorageFeeDetailsUpdateDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "邮储项目类型")
    @NotNull(message = "邮储项目类型不能为空")
    private String postalStorageProjectType;

    @ApiModelProperty(value = "当期分摊金额")
    @NotNull(message = "当期分摊金额不能为空")
    private BigDecimal allocationAmount;

}
