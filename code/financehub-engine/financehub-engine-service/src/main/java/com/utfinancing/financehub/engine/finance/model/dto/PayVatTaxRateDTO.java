package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PayVatTaxRateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回租通用税率")
    private BigDecimal hzGeneralTaxRate;

    @ApiModelProperty(value = "回租手续费税率")
    private BigDecimal hzCommissionTaxRate;

    @ApiModelProperty(value = "回租保险费税率")
    private BigDecimal hzInsuranceTaxRate;

    @ApiModelProperty(value = "直租通用税率")
    private BigDecimal zzGeneralTaxRate;

    @ApiModelProperty(value = "直租手续费税率")
    private BigDecimal zzCommissionTaxRate;

    @ApiModelProperty(value = "直租保险费税率")
    private BigDecimal zzInsuranceTaxRate;

}
