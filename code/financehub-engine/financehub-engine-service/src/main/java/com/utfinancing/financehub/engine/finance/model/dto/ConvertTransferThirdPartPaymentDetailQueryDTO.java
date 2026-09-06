package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ConvertTransferThirdPartPaymentDetailQueryDTO extends BaseQueryDTO {
    /**
     * 批次
     */
    @ApiModelProperty(name = "支付信息id", required = true)
    @NotNull(message = "请选择一个支付信息")
    private Long paymentId;

}
