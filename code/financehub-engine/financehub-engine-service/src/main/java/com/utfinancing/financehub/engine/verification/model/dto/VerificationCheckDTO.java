package com.utfinancing.financehub.engine.verification.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class VerificationCheckDTO {

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate accountDate;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivableDownpayment;

    @ApiModelProperty(value = "应收手续费")
    private BigDecimal receivableCommission;

    @ApiModelProperty(value = "应收保险费")
    private BigDecimal receivableInsurance;

    @ApiModelProperty(value = "应收其他收入")
    private BigDecimal receivableOtherincome;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "应付设备款-暂估")
    private BigDecimal payableDeviceEstimate;

    @ApiModelProperty(value = "应付经销商服务费-暂估")
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估")
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估")
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估")
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估")
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估")
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "应收租赁款补组合拨备")
    private BigDecimal depreciationReserves;

}
