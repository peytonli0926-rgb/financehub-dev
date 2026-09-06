package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemVoucherDTO</li>
 * <li>CreateTime : 2024/04/01 16:06</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class AssetAbsRedeemVoucherDTO extends ExecuteCommonDTO {

    @ApiModelProperty(value = "赎回起算日租金余额")
    private BigDecimal receivableLeaseAmount;

    @ApiModelProperty(value = "赎回起算日留购价余额")
    private BigDecimal retainedPrice;

    @ApiModelProperty(value = "赎回起算日保证金余额")
    private BigDecimal receivableMarginAmount;

    //赎回起算日应收销项税余额，区分客户、合同（余额表凭证日期<=赎回起算日，创建日期为最新时，receivable_outtax_balance的金额）
    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    //赎回起算日未实现收益余额，区分合同（余额表凭证日期<=赎回起算日，创建日期为最新时，unrealized_revenue_balance的金额）
    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "赎回价格")
    private BigDecimal redeemPrice;

    //场景为ZLSK，余额表凭证日期>=赎回起算日时，receivable_unconfirm_receipt_amount的汇总金额
    @ApiModelProperty(value = "未确认收款")
    private BigDecimal receiveUnconfirmed;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_rent_amount的汇总金额
    @ApiModelProperty(value = "收取应收租金")
    private BigDecimal receiveLeaseAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_residual_value_amount的汇总金额
    @ApiModelProperty(value = "收取留购价")
    private BigDecimal receiveRetainedPrice;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，lessee_margin_amount的汇总金额
    @ApiModelProperty(value = "收取承租人保证金")
    private BigDecimal receiveMarginAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，supplier_margin_amount的汇总金额
    @ApiModelProperty(value = "收取供应商保证金")
    private BigDecimal receiveSupplierMarginAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_downpayment_amount的汇总金额
    @ApiModelProperty(value = "收取首付款")
    private BigDecimal receiveDownpaymentAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_commission_amount的汇总金额
    @ApiModelProperty(value = "收取手续费")
    private BigDecimal receiveCommissionAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_service_amount的汇总金额
    @ApiModelProperty(value = "收取服务费")
    private BigDecimal receivesServiceAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_insurance_amount的汇总金额
    @ApiModelProperty(value = "收取保险费")
    private BigDecimal receiveInsuranceAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，insurance_differ_amount的汇总金额
    @ApiModelProperty(value = "收取保险费差额")
    private BigDecimal receiveInsuranceDifferAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_otherincome_amount的汇总金额
    @ApiModelProperty(value = "收取其他收入")
    private BigDecimal receiveOtherincomeAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_damages_revenue_amount的汇总金额
    @ApiModelProperty(value = "收取违约金收入")
    private BigDecimal receiveDamagesRevenueAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_other_revenue_amount的汇总金额
    @ApiModelProperty(value = "收取其他租赁收入")
    private BigDecimal receiveOtherRevenueAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_default_interest_amount的汇总金额
    @ApiModelProperty(value = "收取罚息收入")
    private BigDecimal receiveDefaultInterestAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，receivable_terminate_amount的汇总金额
    @ApiModelProperty(value = "收取合同解约及更改手续费")
    private BigDecimal receiveTerminateProcedureAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，dinterest_revenue_amount的汇总金额
    @ApiModelProperty(value = "罚息收入确认不含税额")
    private BigDecimal dinterestRevenueAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，terminate_amount的汇总金额
    @ApiModelProperty(value = "变更手续费收入确认不含税额")
    private BigDecimal terminateAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，damages_revenue_amount的汇总金额
    @ApiModelProperty(value = "违约金收入确认不含税额")
    private BigDecimal damagesRevenueAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount<=0时，other_revenue_amount的汇总金额
    @ApiModelProperty(value = "其他租赁收入确认不含税额")
    private BigDecimal otherRevenueAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，receivable_outtax_amount>0时的汇总金额
    @ApiModelProperty(value = "税金计提金额")
    private BigDecimal receivableOuttaxAmountJT;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>0时，lessee_margin_amount的汇总金额
    @ApiModelProperty(value = "抵扣保证金")
    private BigDecimal deductionMarginAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，receivable_rent_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣应收租金")
    private BigDecimal deductionLeaseAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，receivable_residual_value_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣留购价")
    private BigDecimal deductionRetainedPrice;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，receivable_default_interest_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣罚息收入")
    private BigDecimal deductionDefaultInterestAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，receivable_terminate_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣合同解约及更改手续费")
    private BigDecimal deductionTerminateProcedureAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，dinterest_revenue_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣罚息收入不含税额")
    private BigDecimal deductionDefaultNoTaxAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，terminate_amount的汇总金额
    @ApiModelProperty(value = "保证金抵扣变更手续费收入不含税额")
    private BigDecimal deductionTerminateNoTaxAmount;

    //场景为ZLSK，余额表凭证日期>=赎回起算日，lessee_margin_amount>时，receivable_outtax_amount>0时的汇总金额
    @ApiModelProperty(value = "保证金抵扣税金计提金额")
    private BigDecimal deductionreceivableOuttaxAmountJT;

}
