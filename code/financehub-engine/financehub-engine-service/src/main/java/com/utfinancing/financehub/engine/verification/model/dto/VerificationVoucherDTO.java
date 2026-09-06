package com.utfinancing.financehub.engine.verification.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.dto.VerificationVoucherDTO</li>
 * <li>CreateTime : 2023/10/17 09:06</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销凭证DTO")
@Data
public class VerificationVoucherDTO {

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    private BigDecimal provisionMix;

    @ApiModelProperty(value = "财务核销敞口")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "补偿提备")
    private BigDecimal compensationProvisionAmount;

    @ApiModelProperty(value = "应付设备款-暂估")
    private BigDecimal payableDeviceEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估")
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付经销商服务费-暂估")
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估")
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

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

    @ApiModelProperty(value = "应付解抵押费_暂估")
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付收车费-暂估")
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估")
    private BigDecimal payableOtherCostEstimate;

}
