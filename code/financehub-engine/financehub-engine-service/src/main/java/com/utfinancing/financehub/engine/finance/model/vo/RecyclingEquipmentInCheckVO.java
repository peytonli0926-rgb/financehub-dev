package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : VO对象
 * @Modified :
 */
@Data
public class RecyclingEquipmentInCheckVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库日期")
    private LocalDateTime inboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

//    @ApiModelProperty(value = "财务合同状态")
//    private String contractStatus;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "未实现融资租赁收益")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "客户代码")
    private String clientCode;

}
