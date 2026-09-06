package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-22
 * @Description : 合同余额表VO对象
 * @Modified :
 */
@Data
public class ParityTransferCheckVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "机构编码")
    private String orgId;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMargin;


}
