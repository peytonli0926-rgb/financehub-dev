package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ConvertTransferCheckVO {

    private String orgId;

    private String contractCode;

    private String clientCode;

    private String clientName;

    @ApiModelProperty("应收租金余额")
    private String receivableRentBalance;

    @ApiModelProperty("应收期末残值发生额")
    private String receivableResidualValueAmount;

    @ApiModelProperty("应收销项税余额")
    private String receivableOuttaxBalance;

    @ApiModelProperty("融资租赁收益余额")
    private String leaseRevenueBalance;

    @ApiModelProperty("承租人保证金余额")
    private String lesseeMarginBalance;

}
