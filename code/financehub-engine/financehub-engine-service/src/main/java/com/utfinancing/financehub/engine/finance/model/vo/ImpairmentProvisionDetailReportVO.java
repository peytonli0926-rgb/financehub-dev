package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细VO对象 本月减值报告
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "分类结果")
    private String classResult;

    @ApiModelProperty(value = "风险敞口")
    private BigDecimal riskExposure;

    @ApiModelProperty(value = "拨备合计")
    private BigDecimal provisionTotal;

    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastMonthBalance;

    @ApiModelProperty(value = "本月转出")
    private BigDecimal thisMonthTransferOut;

    @ApiModelProperty(value = "本月计提")
    private BigDecimal thisMonthProvision;

    @ApiModelProperty(value = "本月计提异常(0-否，1-是)")
    private String thisMonthProvisionError;
}
