package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细汇总VO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailSummaryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "风险敞口合计")
    private BigDecimal riskExposureSummary;

    @ApiModelProperty(value = "本月余额合计")
    private BigDecimal provisionTotalSummary;

    @ApiModelProperty(value = "上月余额合计")
    private BigDecimal lastMonthBalanceSummary;

    @ApiModelProperty(value = "本月计提合计")
    private BigDecimal thisMonthProvisionSummary;

}
