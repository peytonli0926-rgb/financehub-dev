package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ContractAccountBalanceVO {

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate businessDate;

    @ApiModelProperty(value = "凭证日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate voucherDate;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "会计日期")
    private Integer periodCode;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收租金发生额")
    private BigDecimal receivableRentAmount;

    @ApiModelProperty(value = "未实现收益余额")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "未实现收益发生额")
    private BigDecimal unrealizedRevenueAmount;

    @ApiModelProperty(value = "融资租赁收益余额")
    private BigDecimal leaseRevenueBalance;

    @ApiModelProperty(value = "融资租赁收益发生额")
    private BigDecimal leaseRevenueAmount;

    @ApiModelProperty(value = "减值准备余额")
    private BigDecimal depreciationReservesBalance;

    @ApiModelProperty(value = "减值准备发生额")
    private BigDecimal depreciationReservesAmount;

    @ApiModelProperty(value = "应收销项税余额")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "应收销项税发生额")
    private BigDecimal receivableOuttaxAmount;


    @ApiModelProperty(value = "应收服务费-销项税余额")
    private BigDecimal receivableServiceOuttaxBalance;

    @ApiModelProperty(value = "应收服务费-销项税发生额")
    private BigDecimal receivableServiceOuttaxAmount;


    @ApiModelProperty(value = "应付设备款余额")
    private BigDecimal payableDeviceBalances;


    @ApiModelProperty(value = "应付设备款余额")
    private BigDecimal payableDeviceAmounts;

    @ApiModelProperty(value = "承租人保证金余额")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "承租人保证金发生额")
    private BigDecimal lesseeMarginAmount;

}
