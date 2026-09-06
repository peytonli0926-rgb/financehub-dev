package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferThirdPartPaymentQueryDTO extends BaseQueryDTO {
    /**
     * 记账日期 -> 支付日期
     */
    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    @ApiModelProperty("转让批次")
    private String batch;

}
