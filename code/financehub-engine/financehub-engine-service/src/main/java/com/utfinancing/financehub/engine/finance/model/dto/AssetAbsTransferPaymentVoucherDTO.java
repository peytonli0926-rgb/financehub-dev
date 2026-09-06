package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentVoucherDTO</li>
 * <li>CreateTime : 2024/04/01 15:40</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class AssetAbsTransferPaymentVoucherDTO extends ExecuteCommonDTO {

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "实付本金")
    private BigDecimal actualPrincipalAmount;

    @ApiModelProperty(value = "实付利息")
    private BigDecimal actualInterestAmount;

    @ApiModelProperty(value = "实付留够价")
    private BigDecimal actualRetentionPurchaseAmount;

    @ApiModelProperty(value = "实付罚息及手续费")
    private BigDecimal actualPenaltyInterestAmount;

}
