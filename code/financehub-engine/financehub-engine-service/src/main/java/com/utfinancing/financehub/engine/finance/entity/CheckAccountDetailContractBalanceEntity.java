package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_account_detail_contract_balance")
public class CheckAccountDetailContractBalanceEntity extends Model<CheckAccountDetailContractBalanceEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long voucherId;

    private Long interfaceDataId;

    private String systemCode;

    private String businessCode;

    private LocalDateTime businessDate;

    private LocalDateTime voucherDate;

    private String sceneCode;

    private String contractCode;

    private String clientCode;

    private String clientType;

    private String orgId;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private String delFlag;

    private String receivableRentBalance;

    private String receivableRentAmount;

    private String receivableDownpaymentBalance;

    private String receivableDownpaymentAmount;

    private String receivableResidualValueBalance;

    private String receivableResidualValueAmount;

    private String receivableCommissionBalance;

    private String receivableCommissionAmount;

    private String receivableRebateBalance;

    private String receivableRebateAmount;

    private String receivableInsuranceBalance;

    private String receivableInsuranceAmount;

    private String receivableOtherincomeBalance;

    private String receivableOtherincomeAmount;

    private String receivableOuttaxBalance;

    private String receivableOuttaxAmount;

    private String receivableOutputtaxBaseBalance;

    private String receivableOutputtaxBaseAmount;

    private String receivableUnconfirmReceiptBalance;

    private String receivableUnconfirmReceiptAmount;

    private String receivableServiceBalance;

    private String receivableServiceAmount;

    private String receivableServiceOuttaxBalance;

    private String receivableServiceOuttaxAmount;

    private String unrealizedRevenueBalance;

    private String unrealizedRevenueAmount;

    private String leaseRevenueBalance;

    private String leaseRevenueAmount;

    private String serviceRevenueBalance;

    private String serviceRevenueAmount;

    private String insuranceDifferBalance;

    private String insuranceDifferAmount;

    private String dinterestRevenueBalance;

    private String dinterestRevenueAmount;

    private String terminateBalance;

    private String terminateAmount;

    private String otherRevenueBalance;

    private String otherRevenueAmount;

    private String damagesRevenueBalance;

    private String damagesRevenueAmount;

    private String marginInterestBalance;

    private String marginInterestAmount;

    private String payableDeviceEstimateBalance;

    private String payableDeviceEstimateAmount;

    private String payableDeviceBalance;

    private String payableDeviceAmount;

    private String payableOtherCostEstimateBalance;

    private String payableOtherCostEstimateAmount;

    private String payableOtherCostBalance;

    private String payableOtherCostAmount;

    private String payableAgencyEstimateBalance;

    private String payableAgencyEstimateAmount;

    private String payableAgencyBalance;

    private String payableAgencyAmount;

    private String payableVehicleEstimateBalance;

    private String payableVehicleEstimateAmount;

    private String payableVehicleBalance;

    private String payableVehicleAmount;

    private String payableBandCostEstimateBalance;

    private String payableBandCostEstimateAmount;

    private String payableBandCostBalance;

    private String payableBandCostAmount;

    private String payablePledgeEstimateBalance;

    private String payablePledgeEstimateAmount;

    private String payablePledgeBalance;

    private String payablePledgeAmount;

    private String payableUnpledgeEstimateBalance;

    private String payableUnpledgeEstimateAmount;

    private String payableUnpledgeBalance;

    private String payableUnpledgeAmount;

    private String lesseeMarginBalance;

    private String lesseeMarginAmount;

    private String supplierMarginBalance;

    private String supplierMarginAmount;

    private String payableInsuranceEstimateBalance;

    private String payableInsuranceEstimateAmount;

    private String payableInsuranceBalance;

    private String payableInsuranceAmount;

    private String intaxBalance;

    private String intaxAmount;

    private String outtaxBalance;

    private String outtaxAmount;

    private String litigationExpensesBalance;

    private String litigationExpensesAmount;

    private String depreciationReservesBalance;

    private String depreciationReservesAmount;

    private String depreciationLossBalance;

    private String depreciationLossAmount;

    private String receiveCostBalance;

    private String receiveCostAmount;

    private String offIncomeBalance;

    private String offIncomeAmount;

    private String deferIncomeBalance;

    private String deferIncomeAmount;

    private String payableAccountBalance;

    private String payableAccountAmount;

    private String payableMarginBalance;

    private String payableMarginAmount;

    private String payableMarginYearBalance;

    private String payableMarginYearAmount;

    private String marginOinterestBalance;

    private String marginOinterestAmount;

    private String receiveSumBalance;

    private String receiveSumAmount;

    private String receiveSumOuttaxBalance;

    private String receiveSumOuttaxAmount;

    private String receiveUnrealizedRevenueBalance;

    private String receiveUnrealizedRevenueAmount;

    private String payableLesseeMarginYearBalance;

    private String payableLesseeMarginYearAmount;

    private String payableSupplierMarginYearBalance;

    private String payableSupplierMarginYearAmount;

    private String receivableLitigationExpensesBalance;

    private String receivableLitigationExpensesAmount;

    private String payableOtherEstimateBalance;

    private String payableOtherEstimateAmount;

    private String payableDiscountCostBalance;

    private String payableDiscountCostAmount;

    private String payableProcedureCostBalance;

    private String payableProcedureCostAmount;

    private String provisionalReceiptsBalance;

    private String provisionalReceiptsAmount;

    private String equipmentDepreciationReservesBalance;

    private String equipmentDepreciationReservesAmount;

    private String equipmentDepreciationLossBalance;

    private String equipmentDepreciationLossAmount;

    private String assetDisposeGainBalance;

    private String assetDisposeGainAmount;

    private String assetDisposeLossBalance;

    private String assetDisposeLossAmount;

    private String receivableOtherBalance;

    private String receivableOtherAmount;

    private String receivableCollectionTransferBalance;

    private String receivableCollectionTransferAmount;

    private String receivableRelatedPartyBalance;

    private String receivableRelatedPartyAmount;

    private String collectionTransferBalance;

    private String collectionTransferAmount;

    private String collectPaymentOtherBalance;

    private String collectPaymentOtherAmount;

    private String receivableInterestBalance;

    private String receivableInterestAmount;

    private String receivableLitigationMarginBalance;

    private String receivableLitigationMarginAmount;

    private String propertyCostBalance;

    private String propertyCostAmount;

    private String propertyDepreciationReservesBalance;

    private String propertyDepreciationReservesAmount;

    private String machineCostBalance;

    private String machineCostAmount;

    private String machineDepreciationReservesBalance;

    private String machineDepreciationReservesAmount;

    private String otherCostBalance;

    private String otherCostAmount;

    private String otherDepreciationReservesBalance;

    private String otherDepreciationReservesAmount;

    private String receivableFactoringPrincipalBalance;

    private String receivableFactoringPrincipalAmount;

    private String receivableFactoringInterestBalance;

    private String receivableFactoringInterestAmount;

    private String payableDiscountCostOuttaxBalance;

    private String payableDiscountCostOuttaxAmount;

    private String receivableRentInvestmentPropertyBalance;

    private String receivableRentInvestmentPropertyAmount;

    private String receivableOuttaxInvestmentPropertyBalance;

    private String receivableOuttaxInvestmentPropertyAmount;

    private String depreciationReservesTradeBalance;

    private String depreciationReservesTradeAmount;

    private String depreciationReservesServiceBalance;

    private String depreciationReservesServiceAmount;

    private String depreciationReservesOtherReceivableBalance;

    private String depreciationReservesOtherReceivableAmount;

    private String depreciationReservesTempBalance;

    private String depreciationReservesTempAmount;

    private String depreciationReservesOtherMarginBalance;

    private String depreciationReservesOtherMarginAmount;

    private String depreciationReservesInvestmentPropertyBalance;

    private String depreciationReservesInvestmentPropertyAmount;

    private String depreciationReservesDepositBalance;

    private String depreciationReservesDepositAmount;

    private String depreciationReservesLitigationBalance;

    private String depreciationReservesLitigationAmount;

    private String depreciationReservesLitigationMarginBalance;

    private String depreciationReservesLitigationMarginAmount;

    private String depreciationReservesOtherBalance;

    private String depreciationReservesOtherAmount;

    private String depreciationReservesIndividualBalance;

    private String depreciationReservesIndividualAmount;

    private String depreciationReservesBillBalance;

    private String depreciationReservesBillAmount;

    private String depreciationReservesGovBalance;

    private String depreciationReservesGovAmount;

    private String depreciationReservesOtherLongReceiblesBalance;

    private String depreciationReservesOtherLongReceiblesAmount;

    private String depreciationReservesLongRelatedBalance;

    private String depreciationReservesLongRelatedAmount;

    private String depreciationReservesInterestBalance;

    private String depreciationReservesInterestAmount;

    private String receivablePrincipalBalance;

    private String receivablePrincipalAmount;

    private String receivableInterestAdjustmentBalance;

    private String receivableInterestAdjustmentAmount;

    private String receivablePrincipalNonfinancialBalance;

    private String receivablePrincipalNonfinancialAmount;

    private String receivableCommissionNonfinancialBalance;

    private String receivableCommissionNonfinancialAmount;

    private String receivableInterestAdjustmentNonfinancialBalance;

    private String receivableInterestAdjustmentNonfinancialAmount;

    private String depreciationReservesBankBalance;

    private String depreciationReservesBankAmount;

    private String depreciationReservesBuyingBackBalance;

    private String depreciationReservesBuyingBackAmount;

    private String depreciationReservesTermDepositInterestBalance;

    private String depreciationReservesTermDepositInterestAmount;

    private String depreciationReservesBorrowingsInterestBalance;

    private String depreciationReservesBorrowingsInterestAmount;

    private String depreciationReservesBuyingBackInterestBalance;

    private String depreciationReservesBuyingBackInterestAmount;

    private String depreciationReservesFinancialProductInterestBalance;

    private String depreciationReservesFinancialProductInterestAmount;

    private String depreciationReservesStructuredDepositInterestBalance;

    private String depreciationReservesStructuredDepositInterestAmount;

    private String depreciationReservesFvplInterestOtherBalance;

    private String depreciationReservesFvplInterestOtherAmount;

    private String depreciationReservesBondFvociInterestBalance;

    private String depreciationReservesBondFvociInterestAmount;

    private String depreciationReservesFvociInterestOtherBalance;

    private String depreciationReservesFvociInterestOtherAmount;

    private String depreciationReservesBondAcInterestBalance;

    private String depreciationReservesBondAcInterestAmount;

    private String depreciationReservesAcInterestOtherBalance;

    private String depreciationReservesAcInterestOtherAmount;

    private String depreciationReservesBondAcBalance;

    private String depreciationReservesBondAcAmount;

    private String depreciationReservesTurstAcBalance;

    private String depreciationReservesTurstAcAmount;

    private String depreciationReservesOtherFinancialAcBalance;

    private String depreciationReservesOtherFinancialAcAmount;

    private String depreciationReservesOtherAssetAcBalance;

    private String depreciationReservesOtherAssetAcAmount;

    private String depreciationReservesBondFvociBalance;

    private String depreciationReservesBondFvociAmount;

    private String depreciationReservesOtherFinancialFvociBalance;

    private String depreciationReservesOtherFinancialFvociAmount;

    private String depreciationReservesSubsidiaryBalance;

    private String depreciationReservesSubsidiaryAmount;

    private String depreciationReservesJvBalance;

    private String depreciationReservesJvAmount;

    private String depreciationReservesAssociateBalance;

    private String depreciationReservesAssociateAmount;

    private String receivableVirtualBalance;

    private String receivableVirtualAmount;

    private String unrealizedRevenueOtherBalance;

    private String unrealizedRevenueOtherAmount;

    private String payableFactoringBalance;

    private String payableFactoringAmount;

    private String payableEntrustBalance;

    private String payableEntrustAmount;

    private String collectClaimsBalance;

    private String collectClaimsAmount;

    private String payableOtherBalance;

    private String payableOtherAmount;

    private String prereceivedRentBalance;

    private String prereceivedRentAmount;

    private String payableRelatedPartyBalance;

    private String payableRelatedPartyAmount;

    private String otherPayableSpvBalance;

    private String otherPayableSpvAmount;

    private String otherPayableTrustBalance;

    private String otherPayableTrustAmount;

    private String otherPayableClaimAssetBalance;

    private String otherPayableClaimAssetAmount;

    private String otherPayableRentBalance;

    private String otherPayableRentAmount;

    private String otherPayableResidualBalance;

    private String otherPayableResidualAmount;

    private String otherPayableOtherBalance;

    private String otherPayableOtherAmount;

    private String otherPayableClaimBalance;

    private String otherPayableClaimAmount;

    private String otherPayableClaimOtherBalance;

    private String otherPayableClaimOtherAmount;

    private String otherPayableCollectBalance;

    private String otherPayableCollectAmount;

    private String supplierPoolBalance;

    private String supplierPoolAmount;

    private String agentMarginBalance;

    private String agentMarginAmount;

    private String otherMarginBalance;

    private String otherMarginAmount;

    private String rentMarginBalance;

    private String rentMarginAmount;

    private String otherLongMarginBalance;

    private String otherLongMarginAmount;

    private String otherBusinessIncomeBalance;

    private String otherBusinessIncomeAmount;

    private String otherIncomeServiceBalance;

    private String otherIncomeServiceAmount;

    private String interestOtherLongPayablesBalance;

    private String interestOtherLongPayablesAmount;

    private String interestOtherFinancialAcBalance;

    private String interestOtherFinancialAcAmount;

    private String leaseRevenue6Balance;

    private String leaseRevenue6Amount;

    private String leaseRevenue3Balance;

    private String leaseRevenue3Amount;

    private String otherIncomeLeaseTransferBalance;

    private String otherIncomeLeaseTransferAmount;

    private String rentInvestmentPropertyBalance;

    private String rentInvestmentPropertyAmount;

    private String otherIncomeFactoringTransferBalance;

    private String otherIncomeFactoringTransferAmount;

    private String assetDisposeGainForeclosedBalance;

    private String assetDisposeGainForeclosedAmount;

    private String assetDisposeLossForeclosedBalance;

    private String assetDisposeLossForeclosedAmount;

    private String otherCostLeaseTransferBalance;

    private String otherCostLeaseTransferAmount;

    private String otherCostFactoringTransferBalance;

    private String otherCostFactoringTransferAmount;

    private String assessmentFeeBalance;

    private String assessmentFeeAmount;

    private String attorneyFeeBalance;

    private String attorneyFeeAmount;

    private String intermediaryFeeOtherBalance;

    private String intermediaryFeeOtherAmount;

    private String consultationFeeBalance;

    private String consultationFeeAmount;

    private String notaryFeeBalance;

    private String notaryFeeAmount;

    private String leaseAssetRecoveryFeeBalance;

    private String leaseAssetRecoveryFeeAmount;

    private String depreciationLossTradeBalance;

    private String depreciationLossTradeAmount;

    private String depreciationLossServiceBalance;

    private String depreciationLossServiceAmount;

    private String depreciationLossOtherReceivablesBalance;

    private String depreciationLossOtherReceivablesAmount;

    private String depreciationLossTempBalance;

    private String depreciationLossTempAmount;

    private String depreciationLossOtherMarginBalance;

    private String depreciationLossOtherMarginAmount;

    private String depreciationLossRentInvestmentPropertyBalance;

    private String depreciationLossRentInvestmentPropertyAmount;

    private String depreciationLossDepositBalance;

    private String depreciationLossDepositAmount;

    private String depreciationLossLitigationBalance;

    private String depreciationLossLitigationAmount;

    private String depreciationLossLitigationMarginBalance;

    private String depreciationLossLitigationMarginAmount;

    private String depreciationLossOtherReceivableBalance;

    private String depreciationLossOtherReceivableAmount;

    private String depreciationLossReverseBalance;

    private String depreciationLossReverseAmount;

    private String depreciationLossEquityInvestmentBalance;

    private String depreciationLossEquityInvestmentAmount;

    private String depreciationLossInterestBalance;

    private String depreciationLossInterestAmount;

    private String depreciationLossBillBalance;

    private String depreciationLossBillAmount;

    private String depreciationLossCollateralBalance;

    private String depreciationLossCollateralAmount;

    private String depreciationLossFinancialBalance;

    private String depreciationLossFinancialAmount;

    private String depreciationLossBuyingBackBalance;

    private String depreciationLossBuyingBackAmount;

    private String depreciationLossBondAcBalance;

    private String depreciationLossBondAcAmount;

    private String depreciationLossFinancialFvociBalance;

    private String depreciationLossFinancialFvociAmount;

    private String depreciationLossBankBalance;

    private String depreciationLossBankAmount;

    private String depreciationLossTrustAcBalance;

    private String depreciationLossTrustAcAmount;

    private String depreciationLossOtherAssetAcBalance;

    private String depreciationLossOtherAssetAcAmount;

    private String depreciationLossOtherFinancialAcBalance;

    private String depreciationLossOtherFinancialAcAmount;

    private String depreciationLossGovBalance;

    private String depreciationLossGovAmount;

    private String depreciationLossOtherLongReceivablesBalance;

    private String depreciationLossOtherLongReceivablesAmount;

    private String depreciationLossOtherLongRelatedBalance;

    private String depreciationLossOtherLongRelatedAmount;

    private String depreciationLossBorrowingsInterestBalance;

    private String depreciationLossBorrowingsInterestAmount;

    private String depreciationLossInvestmentPropertyBalance;

    private String depreciationLossInvestmentPropertyAmount;

    private String depreciationLossOtherBalance;

    private String depreciationLossOtherAmount;

    private String publicationFeeBalance;

    private String publicationFeeAmount;

    private String receiveSumDebtRestructureBalance;

    private String receiveSumDebtRestructureAmount;

    private String unrealizedRevenueDebtRestructureBalance;

    private String unrealizedRevenueDebtRestructureAmount;

    private String receivableOuttaxDebtRestructureBalance;

    private String receivableOuttaxDebtRestructureAmount;

    private String unrealizedRevenueOtherDebtRestructureBalance;

    private String unrealizedRevenueOtherDebtRestructureAmount;

    private String collectPaymentBalance;

    private String collectPaymentAmount;

    private String receivableDefaultInterestBalance;

    private String receivableDefaultInterestAmount;

    private String receivableTerminateBalance;

    private String receivableTerminateAmount;

    private String litigationVirtualBalance;

    private String litigationVirtualAmount;

    private Integer periodCode;

    private String easVoucherId;

    private String billContractCode;

    private String receivableOtherRevenueBalance;

    private String receivableOtherRevenueAmount;

    private String receivableDamagesRevenueBalance;

    private String receivableDamagesRevenueAmount;

    private String continueInvolvingAssetsBalance;

    private String continueInvolvingAssetsAmount;

    private String payableMarginEntrustedLoansBalance;

    private String payableMarginEntrustedLoansAmount;

    private String continueInvolvingDebtsBalance;

    private String continueInvolvingDebtsAmount;

    private String debtRestructuringIncomeBalance;

    private String debtRestructuringIncomeAmount;

    private String leaseTransferIncomeBalance;

    private String leaseTransferIncomeAmount;

    private String otherBusinessCostBalance;

    private String otherBusinessCostAmount;

    private String financialInstitutionFeeBalance;

    private String financialInstitutionFeeAmount;

    private String payableBillBalance;

    private String payableBillAmount;

    private String receivableBillBalance;

    private String receivableBillAmount;

    private String bankDepositsBalance;

    private String bankDepositsAmount;


}
