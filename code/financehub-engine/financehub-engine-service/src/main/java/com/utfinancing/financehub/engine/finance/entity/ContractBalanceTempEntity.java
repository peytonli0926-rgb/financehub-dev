package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_balance_temp")
public class ContractBalanceTempEntity extends Model<ContractBalanceTempEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //凭证ID
    private Long voucherId;

    //接口表ID
    private Long interfaceDataId;

    //来源系统编码
    private String systemCode;

    //业务编码
    private String businessCode;

    //业务日期
    private LocalDateTime businessDate;

    //凭证日期
    private LocalDateTime voucherDate;

    //场景编码
    private String sceneCode;

    //合同编码
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户类型
    private String clientType;

    //机构编码
    private String orgId;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    private String delFlag;

    //应收租金余额
    private String receivableRentBalance;

    //应收租金发生额
    private String receivableRentAmount;

    //应收首付款余额
    private String receivableDownpaymentBalance;

    //应收首付款发生额
    private String receivableDownpaymentAmount;

    //应收期末残值余额
    private String receivableResidualValueBalance;

    //应收期末残值发生额
    private String receivableResidualValueAmount;

    //应收手续费余额
    private String receivableCommissionBalance;

    //应收手续费发生额
    private String receivableCommissionAmount;

    //应收返利余额
    private String receivableRebateBalance;

    //应收返利发生额
    private String receivableRebateAmount;

    //应收保险费余额
    private String receivableInsuranceBalance;

    //应收保险费发生额
    private String receivableInsuranceAmount;

    //应收其他收入余额
    private String receivableOtherincomeBalance;

    //应收其他收入发生额
    private String receivableOtherincomeAmount;

    //应收销项税余额
    private String receivableOuttaxBalance;

    //应收销项税发生额
    private String receivableOuttaxAmount;

    //应收销项税-本金余额
    private String receivableOutputtaxBaseBalance;

    //应收销项税-本金发生额
    private String receivableOutputtaxBaseAmount;

    //未确认收款余额
    private String receivableUnconfirmReceiptBalance;

    //未确认收款发生额
    private String receivableUnconfirmReceiptAmount;

    //应收服务费余额
    private String receivableServiceBalance;

    //应收服务费发生额
    private String receivableServiceAmount;

    //应收服务费-销项税余额
    private String receivableServiceOuttaxBalance;

    //应收服务费-销项税发生额
    private String receivableServiceOuttaxAmount;

    //未实现收益余额
    private String unrealizedRevenueBalance;

    //未实现收益发生额
    private String unrealizedRevenueAmount;

    //融资租赁收益余额
    private String leaseRevenueBalance;

    //融资租赁收益发生额
    private String leaseRevenueAmount;

    //服务收入余额
    private String serviceRevenueBalance;

    //服务收入发生额
    private String serviceRevenueAmount;

    //保险费差额余额
    private String insuranceDifferBalance;

    //保险费差额发生额
    private String insuranceDifferAmount;

    //罚息收入余额
    private String dinterestRevenueBalance;

    //罚息收入发生额
    private String dinterestRevenueAmount;

    //合同解约及更改手续费余额
    private String terminateBalance;

    //合同解约及更改手续费发生额
    private String terminateAmount;

    //其他租赁相关收入余额
    private String otherRevenueBalance;

    //其他租赁相关收入发生额
    private String otherRevenueAmount;

    //违约金收入余额
    private String damagesRevenueBalance;

    //违约金收入发生额
    private String damagesRevenueAmount;

    //融资租赁业务保证金利息收入余额
    private String marginInterestBalance;

    //融资租赁业务保证金利息收入发生额
    private String marginInterestAmount;

    //应付租赁设备款-暂估余额
    private String payableDeviceEstimateBalance;

    //应付租赁设备款-暂估发生额
    private String payableDeviceEstimateAmount;

    //应付租赁设备款余额
    private String payableDeviceBalance;

    //应付租赁设备款发生额
    private String payableDeviceAmount;

    //应付其他租赁成本-暂估余额
    private String payableOtherCostEstimateBalance;

    //应付其他租赁成本-暂估发生额
    private String payableOtherCostEstimateAmount;

    //应付其他租赁成本余额
    private String payableOtherCostBalance;

    //应付其他租赁成本发生额
    private String payableOtherCostAmount;

    //应付经销商服务费-暂估余额
    private String payableAgencyEstimateBalance;

    //应付经销商服务费-暂估发生额
    private String payableAgencyEstimateAmount;

    //应付经销商服务费余额
    private String payableAgencyBalance;

    //应付经销商服务费发生额
    private String payableAgencyAmount;

    //应付收车费-暂估余额
    private String payableVehicleEstimateBalance;

    //应付收车费-暂估发生额
    private String payableVehicleEstimateAmount;

    //应付收车费余额
    private String payableVehicleBalance;

    //应付收车费发生额
    private String payableVehicleAmount;

    //应付手环成本_暂估余额
    private String payableBandCostEstimateBalance;

    //应付手环成本_暂估发生额
    private String payableBandCostEstimateAmount;

    //应付手环成本余额
    private String payableBandCostBalance;

    //应付手环成本发生额
    private String payableBandCostAmount;

    //应付抵押费_暂估余额
    private String payablePledgeEstimateBalance;

    //应付抵押费_暂估发生额
    private String payablePledgeEstimateAmount;

    //应付抵押费余额
    private String payablePledgeBalance;

    //应付抵押费发生额
    private String payablePledgeAmount;

    //应付解抵押费_暂估余额
    private String payableUnpledgeEstimateBalance;

    //应付解抵押费_暂估发生额
    private String payableUnpledgeEstimateAmount;

    //应付解抵押费余额
    private String payableUnpledgeBalance;

    //应付解抵押费发生额
    private String payableUnpledgeAmount;

    //承租人保证金余额
    private String lesseeMarginBalance;

    //承租人保证金发生额
    private String lesseeMarginAmount;

    //供应商及代理商保证金余额
    private String supplierMarginBalance;

    //供应商及代理商保证金发生额
    private String supplierMarginAmount;

    //应付保险费-暂估余额
    private String payableInsuranceEstimateBalance;

    //应付保险费-暂估发生额
    private String payableInsuranceEstimateAmount;

    //应付保险费余额
    private String payableInsuranceBalance;

    //应付保险费发生额
    private String payableInsuranceAmount;

    //进项税额余额
    private String intaxBalance;

    //进项税额发生额
    private String intaxAmount;

    //销项税额余额
    private String outtaxBalance;

    //销项税额发生额
    private String outtaxAmount;

    //诉讼费余额
    private String litigationExpensesBalance;

    //诉讼费发生额
    private String litigationExpensesAmount;

    //减值准备余额
    private String depreciationReservesBalance;

    //减值准备发生额
    private String depreciationReservesAmount;

    //减值损失余额
    private String depreciationLossBalance;

    //减值损失发生额
    private String depreciationLossAmount;

    //回收融资租赁设备成本余额
    private String receiveCostBalance;

    //回收融资租赁设备成本发生额
    private String receiveCostAmount;

    //表外租赁收入余额
    private String offIncomeBalance;

    //表外租赁收入发生额
    private String offIncomeAmount;

    //递延收益余额
    private String deferIncomeBalance;

    //递延收益发生额
    private String deferIncomeAmount;

    //应付未付款余额
    private String payableAccountBalance;

    //应付未付款发生额
    private String payableAccountAmount;

    //应付保证金余额
    private String payableMarginBalance;

    //应付保证金发生额
    private String payableMarginAmount;

    //应付一年内保证金余额
    private String payableMarginYearBalance;

    //应付一年内保证金发生额
    private String payableMarginYearAmount;

    //保证金利息支出余额
    private String marginOinterestBalance;

    //保证金利息支出发生额
    private String marginOinterestAmount;

    //应收总额余额
    private String receiveSumBalance;

    //应收总额发生额
    private String receiveSumAmount;

    //应收总额_销项税余额
    private String receiveSumOuttaxBalance;

    //应收总额_销项税发生额
    private String receiveSumOuttaxAmount;

    //应收总额_未实现收益余额
    private String receiveUnrealizedRevenueBalance;

    //应收总额_未实现收益发生额
    private String receiveUnrealizedRevenueAmount;

    //应付一年内承租人保证金余额
    private String payableLesseeMarginYearBalance;

    //应付一年内承租人保证金发生额
    private String payableLesseeMarginYearAmount;

    //应付一年内供应商及代理商保证金余额
    private String payableSupplierMarginYearBalance;

    //应付一年内供应商及代理商保证金发生额
    private String payableSupplierMarginYearAmount;

    //应收诉讼费余额
    private String receivableLitigationExpensesBalance;

    //应收诉讼费发生额
    private String receivableLitigationExpensesAmount;

    //应付其他款项-暂估余额
    private String payableOtherEstimateBalance;

    //应付其他款项-暂估发生额
    private String payableOtherEstimateAmount;

    //应收贴息手续费余额
    private String payableDiscountCostBalance;

    //应收贴息手续费发生额
    private String payableDiscountCostAmount;

    //应付手续费成本余额
    private String payableProcedureCostBalance;

    //应付手续费成本发生额
    private String payableProcedureCostAmount;

    //暂收款项余额
    private String provisionalReceiptsBalance;

    //暂收款项发生额
    private String provisionalReceiptsAmount;

    //回收设备减值准备余额
    private String equipmentDepreciationReservesBalance;

    //回收设备减值准备发生额
    private String equipmentDepreciationReservesAmount;

    //回收设备减值损失余额
    private String equipmentDepreciationLossBalance;

    //回收设备减值损失发生额
    private String equipmentDepreciationLossAmount;

    //资产处置收益余额
    private String assetDisposeGainBalance;

    //资产处置收益发生额
    private String assetDisposeGainAmount;

    //资产处置损失余额
    private String assetDisposeLossBalance;

    //资产处置损失发生额
    private String assetDisposeLossAmount;

    //其他应收款余额
    private String receivableOtherBalance;

    //其他应收款发生额
    private String receivableOtherAmount;

    //应收转让后收款余额
    private String receivableCollectionTransferBalance;

    //应收转让后收款发生额
    private String receivableCollectionTransferAmount;

    //其他应收款_关联公司往来余额
    private String receivableRelatedPartyBalance;

    //其他应收款_关联公司往来发生额
    private String receivableRelatedPartyAmount;

    //代收转让款项余额
    private String collectionTransferBalance;

    //代收转让款项发生额
    private String collectionTransferAmount;

    //其他代收款余额
    private String collectPaymentOtherBalance;

    //其他代收款发生额
    private String collectPaymentOtherAmount;

    //应收利息余额
    private String receivableInterestBalance;

    //应收利息发生额
    private String receivableInterestAmount;

    //应收诉讼保证金余额
    private String receivableLitigationMarginBalance;

    //应收诉讼保证金发生额
    private String receivableLitigationMarginAmount;

    //房产_成本余额
    private String propertyCostBalance;

    //房产_成本发生额
    private String propertyCostAmount;

    //房产_减值准备 余额
    private String propertyDepreciationReservesBalance;

    //房产_减值准备 发生额
    private String propertyDepreciationReservesAmount;

    //机器设备_成本余额
    private String machineCostBalance;

    //机器设备_成本发生额
    private String machineCostAmount;

    //机器设备_减值准备余额
    private String machineDepreciationReservesBalance;

    //机器设备_减值准备发生额
    private String machineDepreciationReservesAmount;

    //其他_成本余额
    private String otherCostBalance;

    //其他_成本发生额
    private String otherCostAmount;

    //其他_减值准备余额
    private String otherDepreciationReservesBalance;

    //其他_减值准备发生额
    private String otherDepreciationReservesAmount;

    //应收保理本金余额
    private String receivableFactoringPrincipalBalance;

    //应收保理本金发生额
    private String receivableFactoringPrincipalAmount;

    //应收保理利息调整余额
    private String receivableFactoringInterestBalance;

    //应收保理利息调整发生额
    private String receivableFactoringInterestAmount;

    //应收贴息手续费_销项税余额
    private String payableDiscountCostOuttaxBalance;

    //应收贴息手续费_销项税发生额
    private String payableDiscountCostOuttaxAmount;

    //投资性房地产应收租金	余额
    private String receivableRentInvestmentPropertyBalance;

    //投资性房地产应收租金	发生额
    private String receivableRentInvestmentPropertyAmount;

    //投资性房地产应收销项税	余额
    private String receivableOuttaxInvestmentPropertyBalance;

    //投资性房地产应收销项税	发生额
    private String receivableOuttaxInvestmentPropertyAmount;

    //应收贸易款坏账准备余额
    private String depreciationReservesTradeBalance;

    //应收贸易款坏账准备发生额
    private String depreciationReservesTradeAmount;

    //应收服务费坏账准备余额
    private String depreciationReservesServiceBalance;

    //应收服务费坏账准备发生额
    private String depreciationReservesServiceAmount;

    //其他应收款项坏账准备余额
    private String depreciationReservesOtherReceivableBalance;

    //其他应收款项坏账准备发生额
    private String depreciationReservesOtherReceivableAmount;

    //暂支及个人往来坏账准备余额
    private String depreciationReservesTempBalance;

    //暂支及个人往来坏账准备发生额
    private String depreciationReservesTempAmount;

    //其他保证金坏账准备余额
    private String depreciationReservesOtherMarginBalance;

    //其他保证金坏账准备发生额
    private String depreciationReservesOtherMarginAmount;

    //投资性房地产应收租金坏账准备余额
    private String depreciationReservesInvestmentPropertyBalance;

    //投资性房地产应收租金坏账准备发生额
    private String depreciationReservesInvestmentPropertyAmount;

    //押金坏账准备余额
    private String depreciationReservesDepositBalance;

    //押金坏账准备发生额
    private String depreciationReservesDepositAmount;

    //应收诉讼保全费坏账准备余额
    private String depreciationReservesLitigationBalance;

    //应收诉讼保全费坏账准备发生额
    private String depreciationReservesLitigationAmount;

    //应收诉讼保证金坏账准备余额
    private String depreciationReservesLitigationMarginBalance;

    //应收诉讼保证金坏账准备发生额
    private String depreciationReservesLitigationMarginAmount;

    //其他坏账准备余额
    private String depreciationReservesOtherBalance;

    //其他坏账准备发生额
    private String depreciationReservesOtherAmount;

    //减值准备_单项余额
    private String depreciationReservesIndividualBalance;

    //减值准备_单项发生额
    private String depreciationReservesIndividualAmount;

    //应收票据坏账准备余额
    private String depreciationReservesBillBalance;

    //应收票据坏账准备发生额
    private String depreciationReservesBillAmount;

    //长期应收政府与社会资本合作项目减值准备余额
    private String depreciationReservesGovBalance;

    //长期应收政府与社会资本合作项目减值准备发生额
    private String depreciationReservesGovAmount;

    //其他长期应收款减值准备余额
    private String depreciationReservesOtherLongReceiblesBalance;

    //其他长期应收款减值准备发生额
    private String depreciationReservesOtherLongReceiblesAmount;

    //长期应收款关联公司往来减值准备余额
    private String depreciationReservesLongRelatedBalance;

    //长期应收款关联公司往来减值准备发生额
    private String depreciationReservesLongRelatedAmount;

    //借款应收利息减值准备余额
    private String depreciationReservesInterestBalance;

    //借款应收利息减值准备发生额
    private String depreciationReservesInterestAmount;

    //本金余额
    private String receivablePrincipalBalance;

    //本金发生额
    private String receivablePrincipalAmount;

    //利息调整余额
    private String receivableInterestAdjustmentBalance;

    //利息调整发生额
    private String receivableInterestAdjustmentAmount;

    //贷款_非金融机构_本金余额
    private String receivablePrincipalNonfinancialBalance;

    //贷款_非金融机构_本金发生额
    private String receivablePrincipalNonfinancialAmount;

    //贷款_非金融机构_应收手续费余额
    private String receivableCommissionNonfinancialBalance;

    //贷款_非金融机构_应收手续费发生额
    private String receivableCommissionNonfinancialAmount;

    //贷款_非金融机构_利息调整余额
    private String receivableInterestAdjustmentNonfinancialBalance;

    //贷款_非金融机构_利息调整发生额
    private String receivableInterestAdjustmentNonfinancialAmount;

    //银行存款减值准备余额
    private String depreciationReservesBankBalance;

    //银行存款减值准备发生额
    private String depreciationReservesBankAmount;

    //买入返售金融资产减值准备余额
    private String depreciationReservesBuyingBackBalance;

    //买入返售金融资产减值准备发生额
    private String depreciationReservesBuyingBackAmount;

    //定期存款应收利息减值准备余额
    private String depreciationReservesTermDepositInterestBalance;

    //定期存款应收利息减值准备发生额
    private String depreciationReservesTermDepositInterestAmount;

    //借款应收利息减值准备余额
    private String depreciationReservesBorrowingsInterestBalance;

    //借款应收利息减值准备发生额
    private String depreciationReservesBorrowingsInterestAmount;

    //买入返售金融资产应收利息减值准备余额
    private String depreciationReservesBuyingBackInterestBalance;

    //买入返售金融资产应收利息减值准备发生额
    private String depreciationReservesBuyingBackInterestAmount;

    //银行理财产品应收利息减值准备余额
    private String depreciationReservesFinancialProductInterestBalance;

    //银行理财产品应收利息减值准备发生额
    private String depreciationReservesFinancialProductInterestAmount;

    //结构性存款应收利息减值准备余额
    private String depreciationReservesStructuredDepositInterestBalance;

    //结构性存款应收利息减值准备发生额
    private String depreciationReservesStructuredDepositInterestAmount;

    //FVPL_应收利息减值准备余额
    private String depreciationReservesFvplInterestOtherBalance;

    //FVPL_应收利息减值准备发生额
    private String depreciationReservesFvplInterestOtherAmount;

    //FVOCI_应收利息减值准备余额
    private String depreciationReservesBondFvociInterestBalance;

    //FVOCI_应收利息减值准备发生额
    private String depreciationReservesBondFvociInterestAmount;

    //FVOCI_其他应收利息减值准备余额
    private String depreciationReservesFvociInterestOtherBalance;

    //FVOCI_其他应收利息减值准备发生额
    private String depreciationReservesFvociInterestOtherAmount;

    //以摊余成本计量的债券应收利息减值准备余额
    private String depreciationReservesBondAcInterestBalance;

    //以摊余成本计量的债券应收利息减值准备发生额
    private String depreciationReservesBondAcInterestAmount;

    //其他以摊余成本计量的金融资产应收利息减值准备余额
    private String depreciationReservesAcInterestOtherBalance;

    //其他以摊余成本计量的金融资产应收利息减值准备发生额
    private String depreciationReservesAcInterestOtherAmount;

    //以摊余成本计量的债券减值准备余额
    private String depreciationReservesBondAcBalance;

    //以摊余成本计量的债券减值准备发生额
    private String depreciationReservesBondAcAmount;

    //以摊余成本计量的信托计划减值准备余额
    private String depreciationReservesTurstAcBalance;

    //以摊余成本计量的信托计划减值准备发生额
    private String depreciationReservesTurstAcAmount;

    //以摊余成本计量的其他金融资产减值准备余额
    private String depreciationReservesOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值准备发生额
    private String depreciationReservesOtherFinancialAcAmount;

    //以摊余成本计量的其他资产减值准备余额
    private String depreciationReservesOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值准备发生额
    private String depreciationReservesOtherAssetAcAmount;

    //FVOCI_债券减值准备余额
    private String depreciationReservesBondFvociBalance;

    //FVOCI_债券减值准备发生额
    private String depreciationReservesBondFvociAmount;

    //FVOCI_其他金融资产减值准备余额
    private String depreciationReservesOtherFinancialFvociBalance;

    //FVOCI_其他金融资产减值准备发生额
    private String depreciationReservesOtherFinancialFvociAmount;

    //投资子公司减值准备余额
    private String depreciationReservesSubsidiaryBalance;

    //投资子公司减值准备发生额
    private String depreciationReservesSubsidiaryAmount;

    //投资合营企业减值准备余额
    private String depreciationReservesJvBalance;

    //投资合营企业减值准备发生额
    private String depreciationReservesJvAmount;

    //投资联营企业减值准备余额
    private String depreciationReservesAssociateBalance;

    //投资联营企业减值准备发生额
    private String depreciationReservesAssociateAmount;

    //虚拟收付款余额
    private String receivableVirtualBalance;

    //虚拟收付款发生额
    private String receivableVirtualAmount;

    //未实现其他收益余额
    private String unrealizedRevenueOtherBalance;

    //未实现其他收益发生额
    private String unrealizedRevenueOtherAmount;

    //应付保理款余额
    private String payableFactoringBalance;

    //应付保理款发生额
    private String payableFactoringAmount;

    //应付委贷款余额
    private String payableEntrustBalance;

    //应付委贷款发生额
    private String payableEntrustAmount;

    //代收理赔款余额
    private String collectClaimsBalance;

    //代收理赔款发生额
    private String collectClaimsAmount;

    //应付其他款项余额
    private String payableOtherBalance;

    //应付其他款项发生额
    private String payableOtherAmount;

    //预收租赁款余额
    private String prereceivedRentBalance;

    //预收租赁款发生额
    private String prereceivedRentAmount;

    //其他应付款_关联公司往来	余额
    private String payableRelatedPartyBalance;

    //其他应付款_关联公司往来	发生额
    private String payableRelatedPartyAmount;

    //其他应付款_资产支持专项计划余额
    private String otherPayableSpvBalance;

    //其他应付款_资产支持专项计划发生额
    private String otherPayableSpvAmount;

    //其他应付款_信托计划余额
    private String otherPayableTrustBalance;

    //其他应付款_信托计划发生额
    private String otherPayableTrustAmount;

    //其他应付款_出表保理资产余额
    private String otherPayableClaimAssetBalance;

    //其他应付款_出表保理资产发生额
    private String otherPayableClaimAssetAmount;

    //其他应付款_租金余额余额
    private String otherPayableRentBalance;

    //其他应付款_租金余额发生额
    private String otherPayableRentAmount;

    //其他应付款_残值余额余额
    private String otherPayableResidualBalance;

    //其他应付款_残值余额发生额
    private String otherPayableResidualAmount;

    //其他应付款_其他余额
    private String otherPayableOtherBalance;

    //其他应付款_其他发生额
    private String otherPayableOtherAmount;

    //其他应付款_保理款余额余额
    private String otherPayableClaimBalance;

    //其他应付款_保理款余额发生额
    private String otherPayableClaimAmount;

    //其他应付款_保理款余额_其他余额
    private String otherPayableClaimOtherBalance;

    //其他应付款_保理款余额_其他发生额
    private String otherPayableClaimOtherAmount;

    //其他应付款_代收出表保理资产款项余额
    private String otherPayableCollectBalance;

    //其他应付款_代收出表保理资产款项发生额
    private String otherPayableCollectAmount;

    //供应商资金池余额
    private String supplierPoolBalance;

    //供应商资金池发生额
    private String supplierPoolAmount;

    //代理商保证金余额
    private String agentMarginBalance;

    //代理商保证金发生额
    private String agentMarginAmount;

    //其他保证金余额
    private String otherMarginBalance;

    //其他保证金发生额
    private String otherMarginAmount;

    //房屋租赁保证金余额
    private String rentMarginBalance;

    //房屋租赁保证金发生额
    private String rentMarginAmount;

    //其他长期应付保证金余额
    private String otherLongMarginBalance;

    //其他长期应付保证金发生额
    private String otherLongMarginAmount;

    //其他主营业务收入	余额
    private String otherBusinessIncomeBalance;

    //其他主营业务收入	发生额
    private String otherBusinessIncomeAmount;

    //其他主营业务收入_手续费收入余额
    private String otherIncomeServiceBalance;

    //其他主营业务收入_手续费收入发生额
    private String otherIncomeServiceAmount;

    //其他长期应收款利息收入余额
    private String interestOtherLongPayablesBalance;

    //其他长期应收款利息收入发生额
    private String interestOtherLongPayablesAmount;

    //以摊余成本计量的其他金融资产利息收入余额
    private String interestOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产利息收入发生额
    private String interestOtherFinancialAcAmount;

    //租赁收益6%余额
    private String leaseRevenue6Balance;

    //租赁收益6%发生额
    private String leaseRevenue6Amount;

    //租赁收益3%余额
    private String leaseRevenue3Balance;

    //租赁收益3%发生额
    private String leaseRevenue3Amount;

    //其他业务收入_融资租赁款转让收益余额
    private String otherIncomeLeaseTransferBalance;

    //其他业务收入_融资租赁款转让收益发生额
    private String otherIncomeLeaseTransferAmount;

    //投资性房地产租金收入余额
    private String rentInvestmentPropertyBalance;

    //投资性房地产租金收入发生额
    private String rentInvestmentPropertyAmount;

    //其他业务收入_应收保理款转让收益余额
    private String otherIncomeFactoringTransferBalance;

    //其他业务收入_应收保理款转让收益发生额
    private String otherIncomeFactoringTransferAmount;

    //抵债资产处置收益余额
    private String assetDisposeGainForeclosedBalance;

    //抵债资产处置收益发生额
    private String assetDisposeGainForeclosedAmount;

    //抵债资产处置损失余额
    private String assetDisposeLossForeclosedBalance;

    //抵债资产处置损失发生额
    private String assetDisposeLossForeclosedAmount;

    //其他业务成本_融资租赁款转让收益余额
    private String otherCostLeaseTransferBalance;

    //其他业务成本_融资租赁款转让收益发生额
    private String otherCostLeaseTransferAmount;

    //其他业务成本_应收保理款转让收益余额
    private String otherCostFactoringTransferBalance;

    //其他业务成本_应收保理款转让收益发生额
    private String otherCostFactoringTransferAmount;

    //评估费余额
    private String assessmentFeeBalance;

    //评估费发生额
    private String assessmentFeeAmount;

    //律师费余额
    private String attorneyFeeBalance;

    //律师费发生额
    private String attorneyFeeAmount;

    //其他聘请中介机构费余额
    private String intermediaryFeeOtherBalance;

    //其他聘请中介机构费发生额
    private String intermediaryFeeOtherAmount;

    //咨询费余额
    private String consultationFeeBalance;

    //咨询费发生额
    private String consultationFeeAmount;

    //公证费余额
    private String notaryFeeBalance;

    //公证费发生额
    private String notaryFeeAmount;

    //回收租赁资产杂费余额
    private String leaseAssetRecoveryFeeBalance;

    //回收租赁资产杂费发生额
    private String leaseAssetRecoveryFeeAmount;

    //应收贸易款坏账损失余额
    private String depreciationLossTradeBalance;

    //应收贸易款坏账损失发生额
    private String depreciationLossTradeAmount;

    //应收服务费坏账损失余额
    private String depreciationLossServiceBalance;

    //应收服务费坏账损失发生额
    private String depreciationLossServiceAmount;

    //其他应收款项坏账损失余额
    private String depreciationLossOtherReceivablesBalance;

    //其他应收款项坏账损失发生额
    private String depreciationLossOtherReceivablesAmount;

    //暂支及个人往来坏账损失余额
    private String depreciationLossTempBalance;

    //暂支及个人往来坏账损失发生额
    private String depreciationLossTempAmount;

    //其他保证金坏账损失余额
    private String depreciationLossOtherMarginBalance;

    //其他保证金坏账损失发生额
    private String depreciationLossOtherMarginAmount;

    //投资性房地产应收租金减值损失余额
    private String depreciationLossRentInvestmentPropertyBalance;

    //投资性房地产应收租金减值损失发生额
    private String depreciationLossRentInvestmentPropertyAmount;

    //押金减值损失余额
    private String depreciationLossDepositBalance;

    //押金减值损失发生额
    private String depreciationLossDepositAmount;

    //应收诉讼保全费减值损失余额
    private String depreciationLossLitigationBalance;

    //应收诉讼保全费减值损失发生额
    private String depreciationLossLitigationAmount;

    //应收诉讼保证金减值损失余额
    private String depreciationLossLitigationMarginBalance;

    //应收诉讼保证金减值损失发生额
    private String depreciationLossLitigationMarginAmount;

    //减值损失_其他余额
    private String depreciationLossOtherReceivableBalance;

    //减值损失_其他发生额
    private String depreciationLossOtherReceivableAmount;

    //应收融资租赁款减值损失_坏账注销转回余额
    private String depreciationLossReverseBalance;

    //应收融资租赁款减值损失_坏账注销转回发生额
    private String depreciationLossReverseAmount;

    //长期股权投资减值损失余额
    private String depreciationLossEquityInvestmentBalance;

    //长期股权投资减值损失发生额
    private String depreciationLossEquityInvestmentAmount;

    //应收利息减值损失余额
    private String depreciationLossInterestBalance;

    //应收利息减值损失发生额
    private String depreciationLossInterestAmount;

    //应收票据减值损失余额
    private String depreciationLossBillBalance;

    //应收票据减值损失发生额
    private String depreciationLossBillAmount;

    //抵债资产减值损失余额
    private String depreciationLossCollateralBalance;

    //抵债资产减值损失发生额
    private String depreciationLossCollateralAmount;

    //金融资产减值损失余额
    private String depreciationLossFinancialBalance;

    //金融资产减值损失发生额
    private String depreciationLossFinancialAmount;

    //买入返售金融资产减值损失余额
    private String depreciationLossBuyingBackBalance;

    //买入返售金融资产减值损失发生额
    private String depreciationLossBuyingBackAmount;

    //以摊余成本计量的债券减值损失余额
    private String depreciationLossBondAcBalance;

    //以摊余成本计量的债券减值损失发生额
    private String depreciationLossBondAcAmount;

    //FVOCI_金融资产减值损失余额
    private String depreciationLossFinancialFvociBalance;

    //FVOCI_金融资产减值损失发生额
    private String depreciationLossFinancialFvociAmount;

    //银行存款减值损失余额
    private String depreciationLossBankBalance;

    //银行存款减值损失发生额
    private String depreciationLossBankAmount;

    //以摊余成本计量的信托计划减值损失余额
    private String depreciationLossTrustAcBalance;

    //以摊余成本计量的信托计划减值损失发生额
    private String depreciationLossTrustAcAmount;

    //以摊余成本计量的其他资产减值损失余额
    private String depreciationLossOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值损失发生额
    private String depreciationLossOtherAssetAcAmount;

    //以摊余成本计量的其他金融资产减值损失余额
    private String depreciationLossOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值损失发生额
    private String depreciationLossOtherFinancialAcAmount;

    //长期应收政府与社会资本合作项目减值损失余额
    private String depreciationLossGovBalance;

    //长期应收政府与社会资本合作项目减值损失发生额
    private String depreciationLossGovAmount;

    //其他长期应收款减值损失余额
    private String depreciationLossOtherLongReceivablesBalance;

    //其他长期应收款减值损失发生额
    private String depreciationLossOtherLongReceivablesAmount;

    //长期应收款关联公司往来减值损失余额
    private String depreciationLossOtherLongRelatedBalance;

    //长期应收款关联公司往来减值损失发生额
    private String depreciationLossOtherLongRelatedAmount;

    //借款应收利息减值损失余额
    private String depreciationLossBorrowingsInterestBalance;

    //借款应收利息减值损失发生额
    private String depreciationLossBorrowingsInterestAmount;

    //投资性房地产减值损失余额
    private String depreciationLossInvestmentPropertyBalance;

    //投资性房地产减值损失发生额
    private String depreciationLossInvestmentPropertyAmount;

    //其他减值损失余额
    private String depreciationLossOtherBalance;

    //其他减值损失发生额
    private String depreciationLossOtherAmount;

    //公告费余额
    private String publicationFeeBalance;

    //公告费发生额
    private String publicationFeeAmount;

    //债务重组项目应收总额	余额
    private String receiveSumDebtRestructureBalance;

    //债务重组项目应收总额	发生额
    private String receiveSumDebtRestructureAmount;

    //债务重组项目未实现收益余额
    private String unrealizedRevenueDebtRestructureBalance;

    //债务重组项目未实现收益发生额
    private String unrealizedRevenueDebtRestructureAmount;

    //债务重组项目应收销项税余额
    private String receivableOuttaxDebtRestructureBalance;

    //债务重组项目应收销项税发生额
    private String receivableOuttaxDebtRestructureAmount;

    //债务重组项目未实现其他收益余额
    private String unrealizedRevenueOtherDebtRestructureBalance;

    //债务重组项目未实现其他收益发生额
    private String unrealizedRevenueOtherDebtRestructureAmount;

    //代收款项余额
    private String collectPaymentBalance;

    //代收款项发生额
    private String collectPaymentAmount;

    //应收罚息余额
    private String receivableDefaultInterestBalance;

    //应收罚息发生额
    private String receivableDefaultInterestAmount;

    //应收变更手续费余额
    private String receivableTerminateBalance;

    //应收变更手续费发生额
    private String receivableTerminateAmount;

    //诉讼费支付_虚拟余额
    private String litigationVirtualBalance;

    //诉讼费支付_虚拟发生额
    private String litigationVirtualAmount;

    //会计期间
    private Integer periodCode;

    //金蝶凭证ID
    private String easVoucherId;

    //借款合同编号
    private String billContractCode;

    //应收其他租赁相关收入余额
    private String receivableOtherRevenueBalance;

    //应收其他租赁相关收入发生额
    private String receivableOtherRevenueAmount;

    //应收违约金收入余额
    private String receivableDamagesRevenueBalance;

    //应收违约金收入发生额
    private String receivableDamagesRevenueAmount;

    //继续涉入资产余额
    private String continueInvolvingAssetsBalance;

    //继续涉入资产发生额
    private String continueInvolvingAssetsAmount;

    //应付委托贷款保证金余额
    private String payableMarginEntrustedLoansBalance;

    //应付委托贷款保证金发生额
    private String payableMarginEntrustedLoansAmount;

    //继续涉入负债余额
    private String continueInvolvingDebtsBalance;

    //继续涉入负债发生额
    private String continueInvolvingDebtsAmount;

    //债务重组投资收益余额
    private String debtRestructuringIncomeBalance;

    //债务重组投资收益发生额
    private String debtRestructuringIncomeAmount;

    //融资租赁款转让收益余额
    private String leaseTransferIncomeBalance;

    //融资租赁款转让收益发生额
    private String leaseTransferIncomeAmount;

    //其他业务成本余额
    private String otherBusinessCostBalance;

    //其他业务成本发生额
    private String otherBusinessCostAmount;

    //金融机构手续费余额
    private String financialInstitutionFeeBalance;

    //金融机构手续费发生额
    private String financialInstitutionFeeAmount;

    //应付票据余额
    private String payableBillBalance;

    //应付票据发生额
    private String payableBillAmount;

    //应收票据余额
    private String receivableBillBalance;

    //应收票据发生额
    private String receivableBillAmount;

    //银行存款余额
    @TableField(exist = false)
    private BigDecimal bankDepositsBalance;

    //银行存款发生额
    @TableField(exist = false)
    private BigDecimal bankDepositsAmount;

    //应付款项_财务中台过渡余额
    @TableField(exist = false)
    private BigDecimal payableAccountTransitionBalance;

    //应付款项_财务中台过渡发生额
    @TableField(exist = false)
    private BigDecimal payableAccountTransitionAmount;

    //保证金过渡科目余额
    @TableField(exist = false)
    private BigDecimal marginTransitionBalance;

    //保证金过渡科目发生额
    @TableField(exist = false)
    private BigDecimal marginTransitionAmount;

    @ApiModelProperty("银行存款过渡科目余额")
    @TableField(exist = false)
    private BigDecimal bankDepositsTransitionBalance;

    @ApiModelProperty("银行存款过渡科目发生额")
    @TableField(exist = false)
    private BigDecimal bankDepositsTransitionAmount;

    @ApiModelProperty("应付一年内其他保证金余额")
    @TableField(exist = false)
    private BigDecimal payableOtherMarginYearBalance;

    @ApiModelProperty("应付一年内其他保证金发生额")
    @TableField(exist = false)
    private BigDecimal payableOtherMarginYearAmount;

    @ApiModelProperty("其它应收款项余额")
    @TableField(exist = false)
    private BigDecimal receivableOtherCollectionBalance;

    @ApiModelProperty("其它应收款项发生额")
    @TableField(exist = false)
    private BigDecimal receivableOtherCollectionAmount;
    @ApiModelProperty("其他营业外收入余额")
    @TableField(exist = false)
    private BigDecimal otherIncomeBalance;

    @ApiModelProperty("其他营业外收入发生额")
    @TableField(exist = false)
    private BigDecimal otherIncomeAmount;

    @ApiModelProperty("其它应收款项余额")
    @TableField(exist = false)
    private BigDecimal receivableUnconfirmReceiptTempBalance;

    @ApiModelProperty("其它应收款项发生额")
    @TableField(exist = false)
    private BigDecimal receivableUnconfirmReceiptTempAmount;

    // 华夏金租最新金额类型（数据库映射字段）
    // 银行存款发生额
    private BigDecimal bankDepositAmount;

    // 银行存款余额
    private BigDecimal bankDepositBalance;

    // 应付银行承兑汇票发生额
    private BigDecimal bankAcceptancePayableAmount;

    // 应付银行承兑汇票余额
    private BigDecimal bankAcceptancePayableBalance;

    // 预收租金发生额
    private BigDecimal advanceLeaseReceiptsAmount;

    // 预收租金余额
    private BigDecimal advanceLeaseReceiptsBalance;

    // 其他预收款发生额
    private BigDecimal unidentifiedReceiptsAmount;

    // 其他预收款余额
    private BigDecimal unidentifiedReceiptsBalance;

    // 客户保证金发生额
    private BigDecimal customerDepositPayableAmount;

    // 客户保证金余额
    private BigDecimal customerDepositPayableBalance;

    // 应付供应商款发生额
    private BigDecimal supplierPayableAmount;

    // 应付供应商款余额
    private BigDecimal supplierPayableBalance;

    // 存放同业应收利息发生额
    private BigDecimal interbankInterestReceivableAmount;

    // 存放同业应收利息余额
    private BigDecimal interbankInterestReceivableBalance;

    // 存放同业利息收入发生额
    private BigDecimal interbankInterestIncomeAmount;

    // 存放同业利息收入余额
    private BigDecimal interbankInterestIncomeBalance;

    // 借款利息支出发生额
    private BigDecimal borrowingInterestExpenseAmount;

    // 借款利息支出余额
    private BigDecimal borrowingInterestExpenseBalance;

    // 租赁资产成本发生额
    private BigDecimal leaseAssetCostAmount;

    // 租赁资产成本余额
    private BigDecimal leaseAssetCostBalance;

    // 应收租金发生额
    private BigDecimal leaseRentReceivableAmount;

    // 应收租金余额
    private BigDecimal leaseRentReceivableBalance;

    // 租赁收入发生额
    private BigDecimal leaseRentalIncomeAmount;

    // 租赁收入余额
    private BigDecimal leaseRentalIncomeBalance;

    // 租赁资产折旧费用发生额
    private BigDecimal leaseAssetDepreciationExpenseAmount;

    // 租赁资产折旧费用余额
    private BigDecimal leaseAssetDepreciationExpenseBalance;

    // 租赁资产累计折旧发生额
    private BigDecimal accumulatedLeaseAssetDepreciationAmount;

    // 租赁资产累计折旧余额
    private BigDecimal accumulatedLeaseAssetDepreciationBalance;

    // 租赁资产减值准备发生额
    private BigDecimal leaseAssetImpairmentAllowanceAmount;

    // 租赁资产减值准备余额
    private BigDecimal leaseAssetImpairmentAllowanceBalance;

    // 租赁资产减值损失发生额
    private BigDecimal leaseAssetImpairmentLossAmount;

    // 租赁资产减值损失余额
    private BigDecimal leaseAssetImpairmentLossBalance;

    // 租赁资产处置损益发生额
    private BigDecimal leaseAssetDisposalGainLossAmount;

    // 租赁资产处置损益余额
    private BigDecimal leaseAssetDisposalGainLossBalance;

    // 应收租赁本金发生额
    private BigDecimal leasePrincipalReceivableAmount;

    // 应收租赁本金余额
    private BigDecimal leasePrincipalReceivableBalance;

    // 应收租赁利息发生额
    private BigDecimal leaseInterestReceivableAmount;

    // 应收租赁利息余额
    private BigDecimal leaseInterestReceivableBalance;

    // 应收租赁利息税额发生额
    private BigDecimal leaseInterestVatReceivableAmount;

    // 应收租赁利息税额余额
    private BigDecimal leaseInterestVatReceivableBalance;

    // 应收留购价发生额
    private BigDecimal residualValueReceivableAmount;

    // 应收留购价余额
    private BigDecimal residualValueReceivableBalance;

    // 应收留购价税额发生额
    private BigDecimal residualValueVatReceivableAmount;

    // 应收留购价税额余额
    private BigDecimal residualValueVatReceivableBalance;

    // 未实现融资收益-利息发生额
    private BigDecimal unearnedLeaseInterestAmount;

    // 未实现融资收益-利息余额
    private BigDecimal unearnedLeaseInterestBalance;

    // 未实现融资收益-利息税额发生额
    private BigDecimal unearnedLeaseInterestVatAmount;

    // 未实现融资收益-利息税额余额
    private BigDecimal unearnedLeaseInterestVatBalance;

    // 未实现融资收益-留购价发生额
    private BigDecimal unearnedResidualValueAmount;

    // 未实现融资收益-留购价余额
    private BigDecimal unearnedResidualValueBalance;

    // 未实现融资收益-留购价税额发生额
    private BigDecimal unearnedResidualValueVatAmount;

    // 未实现融资收益-留购价税额余额
    private BigDecimal unearnedResidualValueVatBalance;

    // 逾期租赁本金发生额
    private BigDecimal overdueLeasePrincipalAmount;

    // 逾期租赁本金余额
    private BigDecimal overdueLeasePrincipalBalance;

    // 逾期租赁利息发生额
    private BigDecimal overdueLeaseInterestAmount;

    // 逾期租赁利息余额
    private BigDecimal overdueLeaseInterestBalance;

    // 逾期租赁利息税额发生额
    private BigDecimal overdueLeaseInterestVatAmount;

    // 逾期租赁利息税额余额
    private BigDecimal overdueLeaseInterestVatBalance;

    // 逾期留购价发生额
    private BigDecimal overdueResidualValueAmount;

    // 逾期留购价余额
    private BigDecimal overdueResidualValueBalance;

    // 逾期留购价税额发生额
    private BigDecimal overdueResidualValueVatAmount;

    // 逾期留购价税额余额
    private BigDecimal overdueResidualValueVatBalance;

    // 租赁利息收入发生额
    private BigDecimal leaseInterestIncomeAmount;

    // 租赁利息收入余额
    private BigDecimal leaseInterestIncomeBalance;

    // 罚息收入发生额
    private BigDecimal penaltyInterestIncomeAmount;

    // 罚息收入余额
    private BigDecimal penaltyInterestIncomeBalance;

    // 提前终止收入发生额
    private BigDecimal earlyTerminationIncomeAmount;

    // 提前终止收入余额
    private BigDecimal earlyTerminationIncomeBalance;

    // 提前结清违约金收入发生额
    private BigDecimal earlySettlementPenaltyIncomeAmount;

    // 提前结清违约金收入余额
    private BigDecimal earlySettlementPenaltyIncomeBalance;

    // 应付车辆分润费发生额
    private BigDecimal vehicleProfitSharingPayableAmount;

    // 应付车辆分润费余额
    private BigDecimal vehicleProfitSharingPayableBalance;

    // 应收车辆分润费发生额
    private BigDecimal vehicleProfitSharingReceivableAmount;

    // 应收车辆分润费余额
    private BigDecimal vehicleProfitSharingReceivableBalance;

    // 应付车辆管理费发生额
    private BigDecimal vehicleManagementFeePayableAmount;

    // 应付车辆管理费余额
    private BigDecimal vehicleManagementFeePayableBalance;

    // 车辆项目服务费支出发生额
    private BigDecimal vehicleProjectServiceFeeExpenseAmount;

    // 车辆项目服务费支出余额
    private BigDecimal vehicleProjectServiceFeeExpenseBalance;

    // 车辆零售清分款发生额
    private BigDecimal vehicleClearingPayableAmount;

    // 车辆零售清分款余额
    private BigDecimal vehicleClearingPayableBalance;

    // 银行手续费支出发生额
    private BigDecimal bankServiceFeeExpenseAmount;

    // 银行手续费支出余额
    private BigDecimal bankServiceFeeExpenseBalance;

    // 支付通道手续费支出发生额
    private BigDecimal paymentChannelFeeExpenseAmount;

    // 支付通道手续费支出余额
    private BigDecimal paymentChannelFeeExpenseBalance;

    // 待收增值税进项税发生额
    private BigDecimal inputVatReceivableAmount;

    // 待收增值税进项税余额
    private BigDecimal inputVatReceivableBalance;

    // 待认证进项税额发生额
    private BigDecimal inputVatPendingCertificationAmount;

    // 待认证进项税额余额
    private BigDecimal inputVatPendingCertificationBalance;

    // 应交增值税销项税额发生额
    private BigDecimal outputVatPayableAmount;

    // 应交增值税销项税额余额
    private BigDecimal outputVatPayableBalance;

    // 增值税专票差额发生额
    private BigDecimal vatInvoiceDifferenceAmount;

    // 增值税专票差额余额
    private BigDecimal vatInvoiceDifferenceBalance;

    // 印花税费用发生额
    private BigDecimal stampDutyExpenseAmount;

    // 印花税费用余额
    private BigDecimal stampDutyExpenseBalance;

    // 应交印花税发生额
    private BigDecimal stampDutyPayableAmount;

    // 应交印花税余额
    private BigDecimal stampDutyPayableBalance;

    // 租赁应收款减值准备发生额
    private BigDecimal leaseReceivableImpairmentAllowanceAmount;

    // 租赁应收款减值准备余额
    private BigDecimal leaseReceivableImpairmentAllowanceBalance;

    // 租赁应收款减值损失发生额
    private BigDecimal leaseReceivableImpairmentLossAmount;

    // 租赁应收款减值损失余额
    private BigDecimal leaseReceivableImpairmentLossBalance;

    // 已减值租赁本金发生额
    private BigDecimal impairedLeasePrincipalAmount;

    // 已减值租赁本金余额
    private BigDecimal impairedLeasePrincipalBalance;

    // 已减值租赁利息收入发生额
    private BigDecimal impairedLeaseInterestIncomeAmount;

    // 已减值租赁利息收入余额
    private BigDecimal impairedLeaseInterestIncomeBalance;

    // 已核销租赁本金发生额
    private BigDecimal writtenOffLeasePrincipalAmount;

    // 已核销租赁本金余额
    private BigDecimal writtenOffLeasePrincipalBalance;

    // 表外应收未收利息发生额
    private BigDecimal offBalanceLeaseInterestReceivableAmount;

    // 表外应收未收利息余额
    private BigDecimal offBalanceLeaseInterestReceivableBalance;

    // 表外科目抵消发生额
    private BigDecimal offBalanceOffsetAmount;

    // 表外科目抵消余额
    private BigDecimal offBalanceOffsetBalance;
}
