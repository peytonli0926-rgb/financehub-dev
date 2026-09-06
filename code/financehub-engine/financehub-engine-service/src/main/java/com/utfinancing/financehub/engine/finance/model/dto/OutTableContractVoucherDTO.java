package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.OutTableContractVoucherDTO</li>
 * <li>CreateTime : 2024/03/14 09:59</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("出表ABC凭证DTO")
@Data
public class OutTableContractVoucherDTO extends ExecuteCommonDTO {

    @ApiModelProperty(value = "封包日名义留购价/期末残值")
    private BigDecimal retainedPrice;

    @ApiModelProperty(value = "封包日应收租金")
    private BigDecimal receivableLeaseAmount;

    @ApiModelProperty(value = "封包日应收销项税")
    private BigDecimal receivableOuttaxAmount;

    @ApiModelProperty(value = "封包日未实现融资收益")
    private BigDecimal unrealizedRevenueAmount;

    @ApiModelProperty(value = "封包日承租人保证金")
    private BigDecimal receivableMarginAmount;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "未确认收款")
    private BigDecimal receiveUnconfirmed;

    @ApiModelProperty(value = "收取应收租金")
    private BigDecimal receiveLeaseAmount;

    @ApiModelProperty(value = "收取留购价")
    private BigDecimal receiveRetainedPrice;

    @ApiModelProperty(value = "收取罚息收入")
    private BigDecimal receiveDefaultInterestAmount;

    @ApiModelProperty(value = "收取合同解约及更改手续费")
    private BigDecimal receiveTerminateProcedureAmount;

    @ApiModelProperty(value = "收取承租人保证金")
    private BigDecimal receiveMarginAmount;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttaxAmountKP;

    @ApiModelProperty(value = "应收服务费销项税")
    private BigDecimal receivableServiceOuttaxAmountKP;

    @ApiModelProperty(value = "抵扣保证金")
    private BigDecimal deductionMarginAmount;

    @ApiModelProperty(value = "保证金抵扣应收租金")
    private BigDecimal deductionLeaseAmount;

    @ApiModelProperty(value = "保证金抵扣留购价")
    private BigDecimal deductionRetainedPrice;

    @ApiModelProperty(value = "保证金抵扣罚息收入")
    private BigDecimal deductionDefaultInterestAmount;

    @ApiModelProperty(value = "保证金抵扣合同解约及更改手续费")
    private BigDecimal deductionTerminateProcedureAmount;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "转让损益")
    private BigDecimal transferGainsAndLosses;

    @ApiModelProperty(value = "出表期数")
    private String transferPeriod;

    @ApiModelProperty(value = "发行日应收融资租赁款减值损失")
    private BigDecimal depreciationLoss;

    @ApiModelProperty(value = "发行日应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "租赁收益")
    private BigDecimal leaseRevenue;

    @ApiModelProperty(value = "封包日后租金调整")
    private BigDecimal receivableLeaseAdjustAmount;

    @ApiModelProperty(value = "封包日后首付款调整")
    private BigDecimal firstAdjustAmount;

    @ApiModelProperty(value = "封包日后手续费调整")
    private BigDecimal procedureAdjustRevenues;

    @ApiModelProperty(value = "封包日后应收保险费调整")
    private BigDecimal insuranceAdjustAmount;

    @ApiModelProperty(value = "封包日后应付保险费调整")
    private BigDecimal payableInsuranceEstimateAdjustAmount;

    @ApiModelProperty(value = "封包日后留购价调整")
    private BigDecimal residualAdjustAmount;

    @ApiModelProperty(value = "封包日后其他收入调整")
    private BigDecimal otherAdjustRevenues;

    @ApiModelProperty(value = "封包日后设备款调整")
    private BigDecimal payableDeviceAdjustAmount;

    @ApiModelProperty(value = "封包日后其他成本调整")
    private BigDecimal otherCostAdjustAmount;

    @ApiModelProperty(value = "封包日后经销商服务费调整")
    private BigDecimal payableServiceAdjustAmount;

    @ApiModelProperty(value = "封包日后手环成本调整")
    private BigDecimal payableBraceletAdjustAmount;

    @ApiModelProperty(value = "封包日后应收销项税调整")
    private BigDecimal receivableOuttaxAdjustAmount;

    @ApiModelProperty(value = "封包日后未实现收益调整")
    private BigDecimal unrealizedRevenueAdjustAmount;

    @ApiModelProperty(value = "封包日后服务费调整")
    private BigDecimal serviceAdjustAmount;

    @ApiModelProperty(value = "封包日后服务费销项税调整")
    private BigDecimal receivableServiceOuttaxAdjustAmount;

    @ApiModelProperty(value = "封包日后服务收入调整")
    private BigDecimal serviceEevenueAdjustAmount;

    @ApiModelProperty(value = "封包日后销项税额调整")
    private BigDecimal outtaxAdjustAmount;

    @ApiModelProperty(value = "博远封包日后应收总额调整")
    private BigDecimal receiveSumAdjustAmount;

    @ApiModelProperty(value = "博远封包日后应收销项税调整")
    private BigDecimal receiveSumOuttaxAdjustAmount;

    @ApiModelProperty(value = "博远封包日后应付其他款项调整")
    private BigDecimal payableOtherEstimateAdjustAmount;

    @ApiModelProperty(value = "博远封包日后未实现收益调整")
    private BigDecimal receiveUnrealizedRevenueAdjustAmount;

    @ApiModelProperty(value = "收取首付款")
    private BigDecimal receiveDownpaymentAmount;


    @ApiModelProperty(value = "收取手续费")
    private BigDecimal receiveCommissionAmount;
    @ApiModelProperty(value = "收取服务费")
    private BigDecimal receivesServiceAmount;
    @ApiModelProperty(value = "收取保险费")
    private BigDecimal receiveInsuranceAmount;
    @ApiModelProperty(value = "收取保险费差额")
    private BigDecimal receiveInsuranceDifferAmount;
    @ApiModelProperty(value = "收取其他收入")
    private BigDecimal receiveOtherincomeAmount;
    @ApiModelProperty(value = "收取违约金收入")
    private BigDecimal receiveDamagesRevenueAmount;
    @ApiModelProperty(value = "收取其他租赁收入")
    private BigDecimal receiveOtherRevenueAmount;

    @ApiModelProperty(value = "罚息收入确认不含税额")
    private BigDecimal dinterestRevenueAmount;

    @ApiModelProperty(value = "变更手续费收入确认不含税额")
    private BigDecimal terminateAmount;

    @ApiModelProperty(value = "违约金收入确认不含税额")
    private BigDecimal damagesRevenueAmount;

    @ApiModelProperty(value = "其他租赁收入确认不含税额")
    private BigDecimal otherRevenueAmount;

    @ApiModelProperty(value = "税金计提金额")
    private BigDecimal receivableOuttaxAmountJT;

    @ApiModelProperty(value = "保证金抵扣罚息收入不含税额")
    private BigDecimal deductionDefaultNoTaxAmount;
    @ApiModelProperty(value = "保证金抵扣变更手续费收入不含税额")
    private BigDecimal deductionTerminateNoTaxAmount;
    @ApiModelProperty(value = "保证金抵扣税金计提金额")
    private BigDecimal deductionreceivableOuttaxAmountJT;







}
