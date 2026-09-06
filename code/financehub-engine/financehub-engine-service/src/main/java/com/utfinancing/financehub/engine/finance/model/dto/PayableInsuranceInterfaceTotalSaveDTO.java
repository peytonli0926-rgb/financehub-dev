package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-30
 * @Description : 投保事件累计金额DTO对象
 * @Modified :
 */
@Data
public class PayableInsuranceInterfaceTotalSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "实际计划支付保险费")
    private BigDecimal actualPayableInsuaranceAmount;

    @ApiModelProperty(value = "应付保险费")
    private BigDecimal payableInsuranceAmount;

    @ApiModelProperty(value = "应付保险费余额")
    private BigDecimal payableInsuranceBalance;

    @ApiModelProperty(value = "是否可用1:可用 0:不可用")
    private String enableFlag;

}
