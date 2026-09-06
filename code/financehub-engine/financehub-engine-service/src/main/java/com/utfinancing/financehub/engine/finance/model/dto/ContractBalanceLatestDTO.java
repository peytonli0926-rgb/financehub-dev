package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-30
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class ContractBalanceLatestDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "接口表ID")
    private Long interfaceDataId;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "机构编码")
    private String orgId;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty(value = "应收租金余额")
    private String receivableRentBalance;

    @ApiModelProperty(value = "应收租金发生额")
    private String receivableRentAmount;

    @ApiModelProperty(value = "应收首付款余额")
    private String receivableDownpaymentBalance;

    @ApiModelProperty(value = "应收首付款发生额")
    private String receivableDownpaymentAmount;

    @ApiModelProperty(value = "应收期末残值余额")
    private String receivableResidualValueBalance;

    @ApiModelProperty(value = "应收期末残值发生额")
    private String receivableResidualValueAmount;

    @ApiModelProperty(value = "应收手续费余额")
    private String receivableCommissionBalance;

    @ApiModelProperty(value = "应收手续费发生额")
    private String receivableCommissionAmount;

    @ApiModelProperty(value = "应收返利余额")
    private String receivableRebateBalance;

    @ApiModelProperty(value = "应收返利发生额")
    private String receivableRebateAmount;

    @ApiModelProperty(value = "应收保险费余额")
    private String receivableInsuranceBalance;

    @ApiModelProperty(value = "应收保险费发生额")
    private String receivableInsuranceAmount;

    @ApiModelProperty(value = "应收其他收入余额")
    private String receivableOtherincomeBalance;

    @ApiModelProperty(value = "应收其他收入发生额")
    private String receivableOtherincomeAmount;

    @ApiModelProperty(value = "应收销项税余额")
    private String receivableOuttaxBalance;

    @ApiModelProperty(value = "应收销项税发生额")
    private String receivableOuttaxAmount;

    @ApiModelProperty(value = "应收销项税-本金余额")
    private String receivableOutputtaxBaseBalance;

    @ApiModelProperty(value = "应收销项税-本金发生额")
    private String receivableOutputtaxBaseAmount;

    @ApiModelProperty(value = "未确认收款余额")
    private String receivableUnconfirmReceiptBalance;

    @ApiModelProperty(value = "未确认收款发生额")
    private String receivableUnconfirmReceiptAmount;

    @ApiModelProperty(value = "应收服务费余额")
    private String receivableServiceBalance;

    @ApiModelProperty(value = "应收服务费发生额")
    private String receivableServiceAmount;

    @ApiModelProperty(value = "应收服务费-销项税余额")
    private String receivableServiceOuttaxBalance;

    @ApiModelProperty(value = "应收服务费-销项税发生额")
    private String receivableServiceOuttaxAmount;

    @ApiModelProperty(value = "未实现收益余额")
    private String unrealizedRevenueBalance;

    @ApiModelProperty(value = "未实现收益发生额")
    private String unrealizedRevenueAmount;

    @ApiModelProperty(value = "融资租赁收益余额")
    private String leaseRevenueBalance;

    @ApiModelProperty(value = "融资租赁收益发生额")
    private String leaseRevenueAmount;

    @ApiModelProperty(value = "服务收入余额")
    private String serviceRevenueBalance;

    @ApiModelProperty(value = "服务收入发生额")
    private String serviceRevenueAmount;

    @ApiModelProperty(value = "保险费差额余额")
    private String insuranceDifferBalance;

    @ApiModelProperty(value = "保险费差额发生额")
    private String insuranceDifferAmount;

    @ApiModelProperty(value = "罚息收入余额")
    private String dinterestRevenueBalance;

    @ApiModelProperty(value = "罚息收入发生额")
    private String dinterestRevenueAmount;

    @ApiModelProperty(value = "合同解约及更改手续费余额")
    private String terminateBalance;

    @ApiModelProperty(value = "合同解约及更改手续费发生额")
    private String terminateAmount;

    @ApiModelProperty(value = "其他租赁相关收入余额")
    private String otherRevenueBalance;

    @ApiModelProperty(value = "其他租赁相关收入发生额")
    private String otherRevenueAmount;

    @ApiModelProperty(value = "违约金收入余额")
    private String damagesRevenueBalance;

    @ApiModelProperty(value = "违约金收入发生额")
    private String damagesRevenueAmount;

    @ApiModelProperty(value = "融资租赁业务保证金利息收入余额")
    private String marginInterestBalance;

    @ApiModelProperty(value = "融资租赁业务保证金利息收入发生额")
    private String marginInterestAmount;

    @ApiModelProperty(value = "应付租赁设备款-暂估余额")
    private String payableDeviceEstimateBalance;

    @ApiModelProperty(value = "应付租赁设备款-暂估发生额")
    private String payableDeviceEstimateAmount;

    @ApiModelProperty(value = "应付租赁设备款余额")
    private String payableDeviceBalance;

    @ApiModelProperty(value = "应付租赁设备款发生额")
    private String payableDeviceAmount;

    @ApiModelProperty(value = "应付其他租赁成本-暂估余额")
    private String payableOtherCostEstimateBalance;

    @ApiModelProperty(value = "应付其他租赁成本-暂估发生额")
    private String payableOtherCostEstimateAmount;

    @ApiModelProperty(value = "应付其他租赁成本余额")
    private String payableOtherCostBalance;

    @ApiModelProperty(value = "应付其他租赁成本发生额")
    private String payableOtherCostAmount;

    @ApiModelProperty(value = "应付经销商服务费-暂估余额")
    private String payableAgencyEstimateBalance;

    @ApiModelProperty(value = "应付经销商服务费-暂估发生额")
    private String payableAgencyEstimateAmount;

    @ApiModelProperty(value = "应付经销商服务费余额")
    private String payableAgencyBalance;

    @ApiModelProperty(value = "应付经销商服务费发生额")
    private String payableAgencyAmount;

    @ApiModelProperty(value = "应付收车费-暂估余额")
    private String payableVehicleEstimateBalance;

    @ApiModelProperty(value = "应付收车费-暂估发生额")
    private String payableVehicleEstimateAmount;

    @ApiModelProperty(value = "应付收车费余额")
    private String payableVehicleBalance;

    @ApiModelProperty(value = "应付收车费发生额")
    private String payableVehicleAmount;

    @ApiModelProperty(value = "应付手环成本_暂估余额")
    private String payableBandCostEstimateBalance;

    @ApiModelProperty(value = "应付手环成本_暂估发生额")
    private String payableBandCostEstimateAmount;

    @ApiModelProperty(value = "应付手环成本余额")
    private String payableBandCostBalance;

    @ApiModelProperty(value = "应付手环成本发生额")
    private String payableBandCostAmount;

    @ApiModelProperty(value = "应付抵押费_暂估余额")
    private String payablePledgeEstimateBalance;

    @ApiModelProperty(value = "应付抵押费_暂估发生额")
    private String payablePledgeEstimateAmount;

    @ApiModelProperty(value = "应付抵押费余额")
    private String payablePledgeBalance;

    @ApiModelProperty(value = "应付抵押费发生额")
    private String payablePledgeAmount;

    @ApiModelProperty(value = "应付解抵押费_暂估余额")
    private String payableUnpledgeEstimateBalance;

    @ApiModelProperty(value = "应付解抵押费_暂估发生额")
    private String payableUnpledgeEstimateAmount;

    @ApiModelProperty(value = "应付解抵押费余额")
    private String payableUnpledgeBalance;

    @ApiModelProperty(value = "应付解抵押费发生额")
    private String payableUnpledgeAmount;

    @ApiModelProperty(value = "承租人保证金余额")
    private String lesseeMarginBalance;

    @ApiModelProperty(value = "承租人保证金发生额")
    private String lesseeMarginAmount;

    @ApiModelProperty(value = "供应商及代理商保证金余额")
    private String supplierMarginBalance;

    @ApiModelProperty(value = "供应商及代理商保证金发生额")
    private String supplierMarginAmount;

    @ApiModelProperty(value = "应付保险费-暂估余额")
    private String payableInsuranceEstimateBalance;

    @ApiModelProperty(value = "应付保险费-暂估发生额")
    private String payableInsuranceEstimateAmount;

    @ApiModelProperty(value = "应付保险费余额")
    private String payableInsuranceBalance;

    @ApiModelProperty(value = "应付保险费发生额")
    private String payableInsuranceAmount;

    @ApiModelProperty(value = "进项税额余额")
    private String intaxBalance;

    @ApiModelProperty(value = "进项税额发生额")
    private String intaxAmount;

    @ApiModelProperty(value = "销项税额余额")
    private String outtaxBalance;

    @ApiModelProperty(value = "销项税额发生额")
    private String outtaxAmount;

    @ApiModelProperty(value = "诉讼费余额")
    private String litigationExpensesBalance;

    @ApiModelProperty(value = "诉讼费发生额")
    private String litigationExpensesAmount;

    @ApiModelProperty(value = "减值准备余额")
    private String depreciationReservesBalance;

    @ApiModelProperty(value = "减值准备发生额")
    private String depreciationReservesAmount;

    @ApiModelProperty(value = "减值损失余额")
    private String depreciationLossBalance;

    @ApiModelProperty(value = "减值损失发生额")
    private String depreciationLossAmount;

    @ApiModelProperty(value = "回收融资租赁设备成本余额")
    private String receiveCostBalance;

    @ApiModelProperty(value = "回收融资租赁设备成本发生额")
    private String receiveCostAmount;

    @ApiModelProperty(value = "表外租赁收入余额")
    private String offIncomeBalance;

    @ApiModelProperty(value = "表外租赁收入发生额")
    private String offIncomeAmount;

    @ApiModelProperty(value = "递延收益余额")
    private String deferIncomeBalance;

    @ApiModelProperty(value = "递延收益发生额")
    private String deferIncomeAmount;

    @ApiModelProperty(value = "应付未付款余额")
    private String payableAccountBalance;

    @ApiModelProperty(value = "应付未付款发生额")
    private String payableAccountAmount;

    @ApiModelProperty(value = "应付保证金余额")
    private String payableMarginBalance;

    @ApiModelProperty(value = "应付保证金发生额")
    private String payableMarginAmount;

    @ApiModelProperty(value = "应付一年内保证金余额")
    private String payableMarginYearBalance;

    @ApiModelProperty(value = "应付一年内保证金发生额")
    private String payableMarginYearAmount;

    @ApiModelProperty(value = "保证金利息支出余额")
    private String marginOinterestBalance;

    @ApiModelProperty(value = "保证金利息支出发生额")
    private String marginOinterestAmount;

    @ApiModelProperty(value = "应收总额余额")
    private String receiveSumBalance;

    @ApiModelProperty(value = "应收总额发生额")
    private String receiveSumAmount;

    @ApiModelProperty(value = "应收总额_销项税余额")
    private String receiveSumOuttaxBalance;

    @ApiModelProperty(value = "应收总额_销项税发生额")
    private String receiveSumOuttaxAmount;

    @ApiModelProperty(value = "应收总额_未实现收益余额")
    private String receiveUnrealizedRevenueBalance;

    @ApiModelProperty(value = "应收总额_未实现收益发生额")
    private String receiveUnrealizedRevenueAmount;

    @ApiModelProperty(value = "应付一年内承租人保证金余额")
    private String payableLesseeMarginYearBalance;

    @ApiModelProperty(value = "应付一年内承租人保证金发生额")
    private String payableLesseeMarginYearAmount;

    @ApiModelProperty(value = "应付一年内供应商及代理商保证金余额")
    private String payableSupplierMarginYearBalance;

    @ApiModelProperty(value = "应付一年内供应商及代理商保证金发生额")
    private String payableSupplierMarginYearAmount;

    @ApiModelProperty(value = "应收诉讼费余额")
    private String receivableLitigationExpensesBalance;

    @ApiModelProperty(value = "应收诉讼费发生额")
    private String receivableLitigationExpensesAmount;

    @ApiModelProperty(value = "应付其他款项-暂估余额")
    private String payableOtherEstimateBalance;

    @ApiModelProperty(value = "应付其他款项-暂估发生额")
    private String payableOtherEstimateAmount;

    @ApiModelProperty(value = "应收贴息手续费余额")
    private String payableDiscountCostBalance;

    @ApiModelProperty(value = "应收贴息手续费发生额")
    private String payableDiscountCostAmount;

    @ApiModelProperty(value = "应付手续费成本余额")
    private String payableProcedureCostBalance;

    @ApiModelProperty(value = "应付手续费成本发生额")
    private String payableProcedureCostAmount;

    @ApiModelProperty(value = "暂收款项余额")
    private String provisionalReceiptsBalance;

    @ApiModelProperty(value = "暂收款项发生额")
    private String provisionalReceiptsAmount;

    @ApiModelProperty(value = "回收设备减值准备余额")
    private String equipmentDepreciationReservesBalance;

    @ApiModelProperty(value = "回收设备减值准备发生额")
    private String equipmentDepreciationReservesAmount;

    @ApiModelProperty(value = "回收设备减值损失余额")
    private String equipmentDepreciationLossBalance;

    @ApiModelProperty(value = "回收设备减值损失发生额")
    private String equipmentDepreciationLossAmount;

    @ApiModelProperty(value = "资产处置收益余额")
    private String assetDisposeGainBalance;

    @ApiModelProperty(value = "资产处置收益发生额")
    private String assetDisposeGainAmount;

    @ApiModelProperty(value = "资产处置损失余额")
    private String assetDisposeLossBalance;

    @ApiModelProperty(value = "资产处置损失发生额")
    private String assetDisposeLossAmount;

    @ApiModelProperty(value = "其他应收款余额")
    private String receivableOtherBalance;

    @ApiModelProperty(value = "其他应收款发生额")
    private String receivableOtherAmount;

    @ApiModelProperty(value = "应收转让后收款余额")
    private String receivableCollectionTransferBalance;

    @ApiModelProperty(value = "应收转让后收款发生额")
    private String receivableCollectionTransferAmount;

    @ApiModelProperty(value = "其他应收款_关联公司往来余额")
    private String receivableRelatedPartyBalance;

    @ApiModelProperty(value = "其他应收款_关联公司往来发生额")
    private String receivableRelatedPartyAmount;

    @ApiModelProperty(value = "代收转让款项余额")
    private String collectionTransferBalance;

    @ApiModelProperty(value = "代收转让款项发生额")
    private String collectionTransferAmount;

    @ApiModelProperty(value = "其他代收款余额")
    private String collectPaymentOtherBalance;

    @ApiModelProperty(value = "其他代收款发生额")
    private String collectPaymentOtherAmount;

    @ApiModelProperty(value = "应收利息余额")
    private String receivableInterestBalance;

    @ApiModelProperty(value = "应收利息发生额")
    private String receivableInterestAmount;

    @ApiModelProperty(value = "应收诉讼保证金余额")
    private String receivableLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金发生额")
    private String receivableLitigationMarginAmount;

    @ApiModelProperty(value = "房产_成本余额")
    private String propertyCostBalance;

    @ApiModelProperty(value = "房产_成本发生额")
    private String propertyCostAmount;

    @ApiModelProperty(value = "房产_减值准备 余额")
    private String propertyDepreciationReservesBalance;

    @ApiModelProperty(value = "房产_减值准备 发生额")
    private String propertyDepreciationReservesAmount;

    @ApiModelProperty(value = "机器设备_成本余额")
    private String machineCostBalance;

    @ApiModelProperty(value = "机器设备_成本发生额")
    private String machineCostAmount;

    @ApiModelProperty(value = "机器设备_减值准备余额")
    private String machineDepreciationReservesBalance;

    @ApiModelProperty(value = "机器设备_减值准备发生额")
    private String machineDepreciationReservesAmount;

    @ApiModelProperty(value = "其他_成本余额")
    private String otherCostBalance;

    @ApiModelProperty(value = "其他_成本发生额")
    private String otherCostAmount;

    @ApiModelProperty(value = "其他_减值准备余额")
    private String otherDepreciationReservesBalance;

    @ApiModelProperty(value = "其他_减值准备发生额")
    private String otherDepreciationReservesAmount;

    @ApiModelProperty(value = "应收保理本金余额")
    private String receivableFactoringPrincipalBalance;

    @ApiModelProperty(value = "应收保理本金发生额")
    private String receivableFactoringPrincipalAmount;

    @ApiModelProperty(value = "应收保理利息调整余额")
    private String receivableFactoringInterestBalance;

    @ApiModelProperty(value = "应收保理利息调整发生额")
    private String receivableFactoringInterestAmount;

    @ApiModelProperty(value = "应收贴息手续费_销项税余额")
    private String payableDiscountCostOuttaxBalance;

    @ApiModelProperty(value = "应收贴息手续费_销项税发生额")
    private String payableDiscountCostOuttaxAmount;

    @ApiModelProperty(value = "投资性房地产应收租金	余额")
    private String receivableRentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金	发生额")
    private String receivableRentInvestmentPropertyAmount;

    @ApiModelProperty(value = "投资性房地产应收销项税	余额")
    private String receivableOuttaxInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收销项税	发生额")
    private String receivableOuttaxInvestmentPropertyAmount;

    @ApiModelProperty(value = "应收贸易款坏账准备余额")
    private String depreciationReservesTradeBalance;

    @ApiModelProperty(value = "应收贸易款坏账准备发生额")
    private String depreciationReservesTradeAmount;

    @ApiModelProperty(value = "应收服务费坏账准备余额")
    private String depreciationReservesServiceBalance;

    @ApiModelProperty(value = "应收服务费坏账准备发生额")
    private String depreciationReservesServiceAmount;

    @ApiModelProperty(value = "其他应收款项坏账准备余额")
    private String depreciationReservesOtherReceivableBalance;

    @ApiModelProperty(value = "其他应收款项坏账准备发生额")
    private String depreciationReservesOtherReceivableAmount;

    @ApiModelProperty(value = "暂支及个人往来坏账准备余额")
    private String depreciationReservesTempBalance;

    @ApiModelProperty(value = "暂支及个人往来坏账准备发生额")
    private String depreciationReservesTempAmount;

    @ApiModelProperty(value = "其他保证金坏账准备余额")
    private String depreciationReservesOtherMarginBalance;

    @ApiModelProperty(value = "其他保证金坏账准备发生额")
    private String depreciationReservesOtherMarginAmount;

    @ApiModelProperty(value = "投资性房地产应收租金坏账准备余额")
    private String depreciationReservesInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金坏账准备发生额")
    private String depreciationReservesInvestmentPropertyAmount;

    @ApiModelProperty(value = "押金坏账准备余额")
    private String depreciationReservesDepositBalance;

    @ApiModelProperty(value = "押金坏账准备发生额")
    private String depreciationReservesDepositAmount;

    @ApiModelProperty(value = "应收诉讼保全费坏账准备余额")
    private String depreciationReservesLitigationBalance;

    @ApiModelProperty(value = "应收诉讼保全费坏账准备发生额")
    private String depreciationReservesLitigationAmount;

    @ApiModelProperty(value = "应收诉讼保证金坏账准备余额")
    private String depreciationReservesLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金坏账准备发生额")
    private String depreciationReservesLitigationMarginAmount;

    @ApiModelProperty(value = "其他坏账准备余额")
    private String depreciationReservesOtherBalance;

    @ApiModelProperty(value = "其他坏账准备发生额")
    private String depreciationReservesOtherAmount;

    @ApiModelProperty(value = "减值准备_单项余额")
    private String depreciationReservesIndividualBalance;

    @ApiModelProperty(value = "减值准备_单项发生额")
    private String depreciationReservesIndividualAmount;

    @ApiModelProperty(value = "应收票据坏账准备余额")
    private String depreciationReservesBillBalance;

    @ApiModelProperty(value = "应收票据坏账准备发生额")
    private String depreciationReservesBillAmount;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值准备余额")
    private String depreciationReservesGovBalance;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值准备发生额")
    private String depreciationReservesGovAmount;

    @ApiModelProperty(value = "其他长期应收款减值准备余额")
    private String depreciationReservesOtherLongReceiblesBalance;

    @ApiModelProperty(value = "其他长期应收款减值准备发生额")
    private String depreciationReservesOtherLongReceiblesAmount;

    @ApiModelProperty(value = "长期应收款关联公司往来减值准备余额")
    private String depreciationReservesLongRelatedBalance;

    @ApiModelProperty(value = "长期应收款关联公司往来减值准备发生额")
    private String depreciationReservesLongRelatedAmount;

    @ApiModelProperty(value = "借款应收利息减值准备余额")
    private String depreciationReservesInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值准备发生额")
    private String depreciationReservesInterestAmount;

    @ApiModelProperty(value = "本金余额")
    private String receivablePrincipalBalance;

    @ApiModelProperty(value = "本金发生额")
    private String receivablePrincipalAmount;

    @ApiModelProperty(value = "利息调整余额")
    private String receivableInterestAdjustmentBalance;

    @ApiModelProperty(value = "利息调整发生额")
    private String receivableInterestAdjustmentAmount;

    @ApiModelProperty(value = "贷款_非金融机构_本金余额")
    private String receivablePrincipalNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_本金发生额")
    private String receivablePrincipalNonfinancialAmount;

    @ApiModelProperty(value = "贷款_非金融机构_应收手续费余额")
    private String receivableCommissionNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_应收手续费发生额")
    private String receivableCommissionNonfinancialAmount;

    @ApiModelProperty(value = "贷款_非金融机构_利息调整余额")
    private String receivableInterestAdjustmentNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_利息调整发生额")
    private String receivableInterestAdjustmentNonfinancialAmount;

    @ApiModelProperty(value = "银行存款减值准备余额")
    private String depreciationReservesBankBalance;

    @ApiModelProperty(value = "银行存款减值准备发生额")
    private String depreciationReservesBankAmount;

    @ApiModelProperty(value = "买入返售金融资产减值准备余额")
    private String depreciationReservesBuyingBackBalance;

    @ApiModelProperty(value = "买入返售金融资产减值准备发生额")
    private String depreciationReservesBuyingBackAmount;

    @ApiModelProperty(value = "定期存款应收利息减值准备余额")
    private String depreciationReservesTermDepositInterestBalance;

    @ApiModelProperty(value = "定期存款应收利息减值准备发生额")
    private String depreciationReservesTermDepositInterestAmount;

    @ApiModelProperty(value = "借款应收利息减值准备余额")
    private String depreciationReservesBorrowingsInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值准备发生额")
    private String depreciationReservesBorrowingsInterestAmount;

    @ApiModelProperty(value = "买入返售金融资产应收利息减值准备余额")
    private String depreciationReservesBuyingBackInterestBalance;

    @ApiModelProperty(value = "买入返售金融资产应收利息减值准备发生额")
    private String depreciationReservesBuyingBackInterestAmount;

    @ApiModelProperty(value = "银行理财产品应收利息减值准备余额")
    private String depreciationReservesFinancialProductInterestBalance;

    @ApiModelProperty(value = "银行理财产品应收利息减值准备发生额")
    private String depreciationReservesFinancialProductInterestAmount;

    @ApiModelProperty(value = "结构性存款应收利息减值准备余额")
    private String depreciationReservesStructuredDepositInterestBalance;

    @ApiModelProperty(value = "结构性存款应收利息减值准备发生额")
    private String depreciationReservesStructuredDepositInterestAmount;

    @ApiModelProperty(value = "FVPL_应收利息减值准备余额")
    private String depreciationReservesFvplInterestOtherBalance;

    @ApiModelProperty(value = "FVPL_应收利息减值准备发生额")
    private String depreciationReservesFvplInterestOtherAmount;

    @ApiModelProperty(value = "FVOCI_应收利息减值准备余额")
    private String depreciationReservesBondFvociInterestBalance;

    @ApiModelProperty(value = "FVOCI_应收利息减值准备发生额")
    private String depreciationReservesBondFvociInterestAmount;

    @ApiModelProperty(value = "FVOCI_其他应收利息减值准备余额")
    private String depreciationReservesFvociInterestOtherBalance;

    @ApiModelProperty(value = "FVOCI_其他应收利息减值准备发生额")
    private String depreciationReservesFvociInterestOtherAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券应收利息减值准备余额")
    private String depreciationReservesBondAcInterestBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券应收利息减值准备发生额")
    private String depreciationReservesBondAcInterestAmount;

    @ApiModelProperty(value = "其他以摊余成本计量的金融资产应收利息减值准备余额")
    private String depreciationReservesAcInterestOtherBalance;

    @ApiModelProperty(value = "其他以摊余成本计量的金融资产应收利息减值准备发生额")
    private String depreciationReservesAcInterestOtherAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券减值准备余额")
    private String depreciationReservesBondAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券减值准备发生额")
    private String depreciationReservesBondAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值准备余额")
    private String depreciationReservesTurstAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值准备发生额")
    private String depreciationReservesTurstAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值准备余额")
    private String depreciationReservesOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值准备发生额")
    private String depreciationReservesOtherFinancialAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值准备余额")
    private String depreciationReservesOtherAssetAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值准备发生额")
    private String depreciationReservesOtherAssetAcAmount;

    @ApiModelProperty(value = "FVOCI_债券减值准备余额")
    private String depreciationReservesBondFvociBalance;

    @ApiModelProperty(value = "FVOCI_债券减值准备发生额")
    private String depreciationReservesBondFvociAmount;

    @ApiModelProperty(value = "FVOCI_其他金融资产减值准备余额")
    private String depreciationReservesOtherFinancialFvociBalance;

    @ApiModelProperty(value = "FVOCI_其他金融资产减值准备发生额")
    private String depreciationReservesOtherFinancialFvociAmount;

    @ApiModelProperty(value = "投资子公司减值准备余额")
    private String depreciationReservesSubsidiaryBalance;

    @ApiModelProperty(value = "投资子公司减值准备发生额")
    private String depreciationReservesSubsidiaryAmount;

    @ApiModelProperty(value = "投资合营企业减值准备余额")
    private String depreciationReservesJvBalance;

    @ApiModelProperty(value = "投资合营企业减值准备发生额")
    private String depreciationReservesJvAmount;

    @ApiModelProperty(value = "投资联营企业减值准备余额")
    private String depreciationReservesAssociateBalance;

    @ApiModelProperty(value = "投资联营企业减值准备发生额")
    private String depreciationReservesAssociateAmount;

    @ApiModelProperty(value = "虚拟收付款余额")
    private String receivableVirtualBalance;

    @ApiModelProperty(value = "虚拟收付款发生额")
    private String receivableVirtualAmount;

    @ApiModelProperty(value = "未实现其他收益余额")
    private String unrealizedRevenueOtherBalance;

    @ApiModelProperty(value = "未实现其他收益发生额")
    private String unrealizedRevenueOtherAmount;

    @ApiModelProperty(value = "应付保理款余额")
    private String payableFactoringBalance;

    @ApiModelProperty(value = "应付保理款发生额")
    private String payableFactoringAmount;

    @ApiModelProperty(value = "应付委贷款余额")
    private String payableEntrustBalance;

    @ApiModelProperty(value = "应付委贷款发生额")
    private String payableEntrustAmount;

    @ApiModelProperty(value = "代收理赔款余额")
    private String collectClaimsBalance;

    @ApiModelProperty(value = "代收理赔款发生额")
    private String collectClaimsAmount;

    @ApiModelProperty(value = "应付其他款项余额")
    private String payableOtherBalance;

    @ApiModelProperty(value = "应付其他款项发生额")
    private String payableOtherAmount;

    @ApiModelProperty(value = "预收租赁款余额")
    private String prereceivedRentBalance;

    @ApiModelProperty(value = "预收租赁款发生额")
    private String prereceivedRentAmount;

    @ApiModelProperty(value = "其他应付款_关联公司往来	余额")
    private String payableRelatedPartyBalance;

    @ApiModelProperty(value = "其他应付款_关联公司往来	发生额")
    private String payableRelatedPartyAmount;

    @ApiModelProperty(value = "其他应付款_资产支持专项计划余额")
    private String otherPayableSpvBalance;

    @ApiModelProperty(value = "其他应付款_资产支持专项计划发生额")
    private String otherPayableSpvAmount;

    @ApiModelProperty(value = "其他应付款_信托计划余额")
    private String otherPayableTrustBalance;

    @ApiModelProperty(value = "其他应付款_信托计划发生额")
    private String otherPayableTrustAmount;

    @ApiModelProperty(value = "其他应付款_出表保理资产余额")
    private String otherPayableClaimAssetBalance;

    @ApiModelProperty(value = "其他应付款_出表保理资产发生额")
    private String otherPayableClaimAssetAmount;

    @ApiModelProperty(value = "其他应付款_租金余额余额")
    private String otherPayableRentBalance;

    @ApiModelProperty(value = "其他应付款_租金余额发生额")
    private String otherPayableRentAmount;

    @ApiModelProperty(value = "其他应付款_残值余额余额")
    private String otherPayableResidualBalance;

    @ApiModelProperty(value = "其他应付款_残值余额发生额")
    private String otherPayableResidualAmount;

    @ApiModelProperty(value = "其他应付款_其他余额")
    private String otherPayableOtherBalance;

    @ApiModelProperty(value = "其他应付款_其他发生额")
    private String otherPayableOtherAmount;

    @ApiModelProperty(value = "其他应付款_保理款余额余额")
    private String otherPayableClaimBalance;

    @ApiModelProperty(value = "其他应付款_保理款余额发生额")
    private String otherPayableClaimAmount;

    @ApiModelProperty(value = "其他应付款_保理款余额_其他余额")
    private String otherPayableClaimOtherBalance;

    @ApiModelProperty(value = "其他应付款_保理款余额_其他发生额")
    private String otherPayableClaimOtherAmount;

    @ApiModelProperty(value = "其他应付款_代收出表保理资产款项余额")
    private String otherPayableCollectBalance;

    @ApiModelProperty(value = "其他应付款_代收出表保理资产款项发生额")
    private String otherPayableCollectAmount;

    @ApiModelProperty(value = "供应商资金池余额")
    private String supplierPoolBalance;

    @ApiModelProperty(value = "供应商资金池发生额")
    private String supplierPoolAmount;

    @ApiModelProperty(value = "代理商保证金余额")
    private String agentMarginBalance;

    @ApiModelProperty(value = "代理商保证金发生额")
    private String agentMarginAmount;

    @ApiModelProperty(value = "其他保证金余额")
    private String otherMarginBalance;

    @ApiModelProperty(value = "其他保证金发生额")
    private String otherMarginAmount;

    @ApiModelProperty(value = "房屋租赁保证金余额")
    private String rentMarginBalance;

    @ApiModelProperty(value = "房屋租赁保证金发生额")
    private String rentMarginAmount;

    @ApiModelProperty(value = "其他长期应付保证金余额")
    private String otherLongMarginBalance;

    @ApiModelProperty(value = "其他长期应付保证金发生额")
    private String otherLongMarginAmount;

    @ApiModelProperty(value = "其他主营业务收入	余额")
    private String otherBusinessIncomeBalance;

    @ApiModelProperty(value = "其他主营业务收入	发生额")
    private String otherBusinessIncomeAmount;

    @ApiModelProperty(value = "其他主营业务收入_手续费收入余额")
    private String otherIncomeServiceBalance;

    @ApiModelProperty(value = "其他主营业务收入_手续费收入发生额")
    private String otherIncomeServiceAmount;

    @ApiModelProperty(value = "其他长期应收款利息收入余额")
    private String interestOtherLongPayablesBalance;

    @ApiModelProperty(value = "其他长期应收款利息收入发生额")
    private String interestOtherLongPayablesAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产利息收入余额")
    private String interestOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产利息收入发生额")
    private String interestOtherFinancialAcAmount;

    @ApiModelProperty(value = "租赁收益6%余额")
    private String leaseRevenue6Balance;

    @ApiModelProperty(value = "租赁收益6%发生额")
    private String leaseRevenue6Amount;

    @ApiModelProperty(value = "租赁收益3%余额")
    private String leaseRevenue3Balance;

    @ApiModelProperty(value = "租赁收益3%发生额")
    private String leaseRevenue3Amount;

    @ApiModelProperty(value = "其他业务收入_融资租赁款转让收益余额")
    private String otherIncomeLeaseTransferBalance;

    @ApiModelProperty(value = "其他业务收入_融资租赁款转让收益发生额")
    private String otherIncomeLeaseTransferAmount;

    @ApiModelProperty(value = "投资性房地产租金收入余额")
    private String rentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产租金收入发生额")
    private String rentInvestmentPropertyAmount;

    @ApiModelProperty(value = "其他业务收入_应收保理款转让收益余额")
    private String otherIncomeFactoringTransferBalance;

    @ApiModelProperty(value = "其他业务收入_应收保理款转让收益发生额")
    private String otherIncomeFactoringTransferAmount;

    @ApiModelProperty(value = "抵债资产处置收益余额")
    private String assetDisposeGainForeclosedBalance;

    @ApiModelProperty(value = "抵债资产处置收益发生额")
    private String assetDisposeGainForeclosedAmount;

    @ApiModelProperty(value = "抵债资产处置损失余额")
    private String assetDisposeLossForeclosedBalance;

    @ApiModelProperty(value = "抵债资产处置损失发生额")
    private String assetDisposeLossForeclosedAmount;

    @ApiModelProperty(value = "其他业务成本_融资租赁款转让收益余额")
    private String otherCostLeaseTransferBalance;

    @ApiModelProperty(value = "其他业务成本_融资租赁款转让收益发生额")
    private String otherCostLeaseTransferAmount;

    @ApiModelProperty(value = "其他业务成本_应收保理款转让收益余额")
    private String otherCostFactoringTransferBalance;

    @ApiModelProperty(value = "其他业务成本_应收保理款转让收益发生额")
    private String otherCostFactoringTransferAmount;

    @ApiModelProperty(value = "评估费余额")
    private String assessmentFeeBalance;

    @ApiModelProperty(value = "评估费发生额")
    private String assessmentFeeAmount;

    @ApiModelProperty(value = "律师费余额")
    private String attorneyFeeBalance;

    @ApiModelProperty(value = "律师费发生额")
    private String attorneyFeeAmount;

    @ApiModelProperty(value = "其他聘请中介机构费余额")
    private String intermediaryFeeOtherBalance;

    @ApiModelProperty(value = "其他聘请中介机构费发生额")
    private String intermediaryFeeOtherAmount;

    @ApiModelProperty(value = "咨询费余额")
    private String consultationFeeBalance;

    @ApiModelProperty(value = "咨询费发生额")
    private String consultationFeeAmount;

    @ApiModelProperty(value = "公证费余额")
    private String notaryFeeBalance;

    @ApiModelProperty(value = "公证费发生额")
    private String notaryFeeAmount;

    @ApiModelProperty(value = "回收租赁资产杂费余额")
    private String leaseAssetRecoveryFeeBalance;

    @ApiModelProperty(value = "回收租赁资产杂费发生额")
    private String leaseAssetRecoveryFeeAmount;

    @ApiModelProperty(value = "应收贸易款坏账损失余额")
    private String depreciationLossTradeBalance;

    @ApiModelProperty(value = "应收贸易款坏账损失发生额")
    private String depreciationLossTradeAmount;

    @ApiModelProperty(value = "应收服务费坏账损失余额")
    private String depreciationLossServiceBalance;

    @ApiModelProperty(value = "应收服务费坏账损失发生额")
    private String depreciationLossServiceAmount;

    @ApiModelProperty(value = "其他应收款项坏账损失余额")
    private String depreciationLossOtherReceivablesBalance;

    @ApiModelProperty(value = "其他应收款项坏账损失发生额")
    private String depreciationLossOtherReceivablesAmount;

    @ApiModelProperty(value = "暂支及个人往来坏账损失余额")
    private String depreciationLossTempBalance;

    @ApiModelProperty(value = "暂支及个人往来坏账损失发生额")
    private String depreciationLossTempAmount;

    @ApiModelProperty(value = "其他保证金坏账损失余额")
    private String depreciationLossOtherMarginBalance;

    @ApiModelProperty(value = "其他保证金坏账损失发生额")
    private String depreciationLossOtherMarginAmount;

    @ApiModelProperty(value = "投资性房地产应收租金减值损失余额")
    private String depreciationLossRentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金减值损失发生额")
    private String depreciationLossRentInvestmentPropertyAmount;

    @ApiModelProperty(value = "押金减值损失余额")
    private String depreciationLossDepositBalance;

    @ApiModelProperty(value = "押金减值损失发生额")
    private String depreciationLossDepositAmount;

    @ApiModelProperty(value = "应收诉讼保全费减值损失余额")
    private String depreciationLossLitigationBalance;

    @ApiModelProperty(value = "应收诉讼保全费减值损失发生额")
    private String depreciationLossLitigationAmount;

    @ApiModelProperty(value = "应收诉讼保证金减值损失余额")
    private String depreciationLossLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金减值损失发生额")
    private String depreciationLossLitigationMarginAmount;

    @ApiModelProperty(value = "减值损失_其他余额")
    private String depreciationLossOtherReceivableBalance;

    @ApiModelProperty(value = "减值损失_其他发生额")
    private String depreciationLossOtherReceivableAmount;

    @ApiModelProperty(value = "应收融资租赁款减值损失_坏账注销转回余额")
    private String depreciationLossReverseBalance;

    @ApiModelProperty(value = "应收融资租赁款减值损失_坏账注销转回发生额")
    private String depreciationLossReverseAmount;

    @ApiModelProperty(value = "长期股权投资减值损失余额")
    private String depreciationLossEquityInvestmentBalance;

    @ApiModelProperty(value = "长期股权投资减值损失发生额")
    private String depreciationLossEquityInvestmentAmount;

    @ApiModelProperty(value = "应收利息减值损失余额")
    private String depreciationLossInterestBalance;

    @ApiModelProperty(value = "应收利息减值损失发生额")
    private String depreciationLossInterestAmount;

    @ApiModelProperty(value = "应收票据减值损失余额")
    private String depreciationLossBillBalance;

    @ApiModelProperty(value = "应收票据减值损失发生额")
    private String depreciationLossBillAmount;

    @ApiModelProperty(value = "抵债资产减值损失余额")
    private String depreciationLossCollateralBalance;

    @ApiModelProperty(value = "抵债资产减值损失发生额")
    private String depreciationLossCollateralAmount;

    @ApiModelProperty(value = "金融资产减值损失余额")
    private String depreciationLossFinancialBalance;

    @ApiModelProperty(value = "金融资产减值损失发生额")
    private String depreciationLossFinancialAmount;

    @ApiModelProperty(value = "买入返售金融资产减值损失余额")
    private String depreciationLossBuyingBackBalance;

    @ApiModelProperty(value = "买入返售金融资产减值损失发生额")
    private String depreciationLossBuyingBackAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券减值损失余额")
    private String depreciationLossBondAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券减值损失发生额")
    private String depreciationLossBondAcAmount;

    @ApiModelProperty(value = "FVOCI_金融资产减值损失余额")
    private String depreciationLossFinancialFvociBalance;

    @ApiModelProperty(value = "FVOCI_金融资产减值损失发生额")
    private String depreciationLossFinancialFvociAmount;

    @ApiModelProperty(value = "银行存款减值损失余额")
    private String depreciationLossBankBalance;

    @ApiModelProperty(value = "银行存款减值损失发生额")
    private String depreciationLossBankAmount;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值损失余额")
    private String depreciationLossTrustAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值损失发生额")
    private String depreciationLossTrustAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值损失余额")
    private String depreciationLossOtherAssetAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值损失发生额")
    private String depreciationLossOtherAssetAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值损失余额")
    private String depreciationLossOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值损失发生额")
    private String depreciationLossOtherFinancialAcAmount;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值损失余额")
    private String depreciationLossGovBalance;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值损失发生额")
    private String depreciationLossGovAmount;

    @ApiModelProperty(value = "其他长期应收款减值损失余额")
    private String depreciationLossOtherLongReceivablesBalance;

    @ApiModelProperty(value = "其他长期应收款减值损失发生额")
    private String depreciationLossOtherLongReceivablesAmount;

    @ApiModelProperty(value = "长期应收款关联公司往来减值损失余额")
    private String depreciationLossOtherLongRelatedBalance;

    @ApiModelProperty(value = "长期应收款关联公司往来减值损失发生额")
    private String depreciationLossOtherLongRelatedAmount;

    @ApiModelProperty(value = "借款应收利息减值损失余额")
    private String depreciationLossBorrowingsInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值损失发生额")
    private String depreciationLossBorrowingsInterestAmount;

    @ApiModelProperty(value = "投资性房地产减值损失余额")
    private String depreciationLossInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产减值损失发生额")
    private String depreciationLossInvestmentPropertyAmount;

    @ApiModelProperty(value = "其他减值损失余额")
    private String depreciationLossOtherBalance;

    @ApiModelProperty(value = "其他减值损失发生额")
    private String depreciationLossOtherAmount;

    @ApiModelProperty(value = "公告费余额")
    private String publicationFeeBalance;

    @ApiModelProperty(value = "公告费发生额")
    private String publicationFeeAmount;

    @ApiModelProperty(value = "债务重组项目应收总额	余额")
    private String receiveSumDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目应收总额	发生额")
    private String receiveSumDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目未实现收益余额")
    private String unrealizedRevenueDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目未实现收益发生额")
    private String unrealizedRevenueDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目应收销项税余额")
    private String receivableOuttaxDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目应收销项税发生额")
    private String receivableOuttaxDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目未实现其他收益余额")
    private String unrealizedRevenueOtherDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目未实现其他收益发生额")
    private String unrealizedRevenueOtherDebtRestructureAmount;

    @ApiModelProperty(value = "代收款项余额")
    private String collectPaymentBalance;

    @ApiModelProperty(value = "代收款项发生额")
    private String collectPaymentAmount;

    @ApiModelProperty(value = "应收罚息余额")
    private String receivableDefaultInterestBalance;

    @ApiModelProperty(value = "应收罚息发生额")
    private String receivableDefaultInterestAmount;

    @ApiModelProperty(value = "应收变更手续费余额")
    private String receivableTerminateBalance;

    @ApiModelProperty(value = "应收变更手续费发生额")
    private String receivableTerminateAmount;

    @ApiModelProperty(value = "诉讼费支付_虚拟余额")
    private String litigationVirtualBalance;

    @ApiModelProperty(value = "诉讼费支付_虚拟发生额")
    private String litigationVirtualAmount;

    @ApiModelProperty(value = "银行存款余额")
    private BigDecimal bankDepositsBalance;

    @ApiModelProperty(value = "银行存款发生额")
    private BigDecimal bankDepositsAmount;

    @ApiModelProperty(value = "应付款项_财务中台过渡余额")
    private BigDecimal payableAccountTransitionBalance;

    @ApiModelProperty(value = "应付款项_财务中台过渡发生额")
    private BigDecimal payableAccountTransitionAmount;

    @ApiModelProperty("保证金过渡科目余额")
    private BigDecimal marginTransitionBalance;

    @ApiModelProperty("保证金过渡科目发生额")
    private BigDecimal marginTransitionAmount;

    @ApiModelProperty("银行存款过渡科目余额")
    private BigDecimal bankDepositsTransitionBalance;

    @ApiModelProperty("银行存款过渡科目发生额")
    private BigDecimal bankDepositsTransitionAmount;

    @ApiModelProperty("应付一年内其他保证金余额")
    private BigDecimal payableOtherMarginYearBalance;

    @ApiModelProperty("应付一年内其他保证金发生额")
    private BigDecimal payableOtherMarginYearAmount;

    @ApiModelProperty("其它应收款项余额")
    private BigDecimal receivableOtherCollectionBalance;

    @ApiModelProperty("其它应收款项发生额")
    private BigDecimal receivableOtherCollectionAmount;

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
