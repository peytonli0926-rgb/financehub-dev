package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoucherCopyDTO {

    @NotNull(message = "凭证id不可以为空")
    private Long voucherId;

    @NotNull(message = "数据来源不可以为空")
    @ApiModelProperty(value = "数据来源0:凭证表，1：手工表")
    private String sourceFromType;
}
