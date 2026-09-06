package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-09
 * @Description : 合同交易结构接口表累计金额DTO对象
 * @Modified :
 */
@Data
public class ContractChangeSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "应收租金调整额")
    private BigDecimal receivableLeaseAdjustAmount;
    @ApiModelProperty(value = "首付款调整额")
    private BigDecimal firstAdjustAmount;
    @ApiModelProperty(value = "手续费收入调整额")
    private BigDecimal procedureAdjustRevenues;
    @ApiModelProperty(value = "应收保险费调整额")
    private BigDecimal insuranceAdjustAmount;
    @ApiModelProperty(value = "履约保证金调整额")
    private BigDecimal implementMarginAdjustAmount;
    @ApiModelProperty(value = "残值调整额")
    private BigDecimal residualAdjustAmount;
    @ApiModelProperty(value = "其它收入调整额")
    private BigDecimal otherAdjustRevenues;
    @ApiModelProperty(value = "长期应收款-未确认收款")
    private BigDecimal receivable_unconfirm_receipt;
    @ApiModelProperty(value = "咨询服务收入")
    private BigDecimal service_revenue;
    @ApiModelProperty(value = "厂商返利调整额")
    private BigDecimal firmAdjustRebate;
    @ApiModelProperty(value = "其它成本调整额")
    private BigDecimal otherCostAdjustAmount;
    @ApiModelProperty(value = "应付渠道费用调整额")
    private BigDecimal channelAdjustExpense;
    @ApiModelProperty(value = "应付海通渠道费用调整额")
    private BigDecimal innerAdjustExpense;
    @ApiModelProperty(value = "应付设备租赁款调整额")
    private BigDecimal payableDeviceAdjustAmount;
    @ApiModelProperty(value = "应付经销商服务费调整额")
    private BigDecimal serviceAdjustAmount;
    @ApiModelProperty(value = "保证金调整额")
    private BigDecimal marginAdjustAmount;
    @ApiModelProperty(value = "应付手环成本调整额")
    private BigDecimal payableBraceletAdjustAmount;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableLeaseAmount;
    @ApiModelProperty(value = "应付租赁设备款发生额")
    private BigDecimal payableDeviceAmount;
    @ApiModelProperty(value = "应收本金调整额")
    private BigDecimal receivablePrincipalAdjustAmount;
    @ApiModelProperty(value = "应收利息调整额")
    private BigDecimal receivableInterestAdjustAmount;
    @ApiModelProperty(value = "减额本金发生额")
    private BigDecimal deratePrincipalAmount;
    @ApiModelProperty(value = "减额利息发生额")
    private BigDecimal derateInterestAmount;
}
