package com.utfinancing.financehub.engine.verification.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO</li>
 * <li>CreateTime : 2023/10/19 09:36</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销回款VO")
@Data
public class VerificationPaybackVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回款月份")
    private String businessDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "拨备转回金额")
    private BigDecimal reversalProvisionAmount;

    @ApiModelProperty(value = "确认收入金融")
    private BigDecimal revenueFinance;

}
