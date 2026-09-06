package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ConvertTransferThirdPartCheckVO {

    @ApiModelProperty("签约主体")
    private String orgId;

    @ApiModelProperty("合同编码")
    private String contractCode;

    @ApiModelProperty("客户编码")
    private String clientCode;
    @ApiModelProperty("客户名称")
    private String clientName;

    @ApiModelProperty("应收租金余额")
    private String receivableRentBalance;

    @ApiModelProperty("应收期末残值发生额")
    private String receivableResidualValueAmount;

    @ApiModelProperty("应收销项税余额")
    private String receivableOuttaxBalance;

    @ApiModelProperty("未实现融资租赁收益余额")
    private String leaseRevenueBalance;

    @ApiModelProperty("承租人保证金余额")
    private String lesseeMarginBalance;

    @ApiModelProperty("应收合同解约及变更手续费")
    private String receivableTerminateAmount;

    @ApiModelProperty("应收罚息")
    private String receivableDefaultInterestAmount;

    @ApiModelProperty("应收融资租赁款组合拨备")
    private String depreciationReservesAmount;

}
