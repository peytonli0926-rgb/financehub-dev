package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferThirdPartPaymentGenerateDTO {
    @ApiModelProperty("批次")
    @NotBlank(message = "请选择一个批次")
    private String batch;

    @ApiModelProperty(value = "财务日期", example = "2024-12-20")
    @NotNull(message = "请选择支付日期")
    private LocalDate paymentDate;
}
