package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-22
 * @Description : 合同余额表DTO对象
 * @Modified :
 */
@Data
public class ContractBalanceDTO implements Serializable{
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
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收租金发生额")
    private BigDecimal receivableRentAmount;

    @ApiModelProperty(value = "应收首付款余额")
    private BigDecimal receivableDownpaymentBalance;

    @ApiModelProperty(value = "应收首付款发生额")
    private BigDecimal receivableDownpaymentAmount;

    @ApiModelProperty(value = "应收期末残值余额")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收期末残值发生额")
    private BigDecimal receivableResidualValueAmount;

    @ApiModelProperty(value = "应收手续费余额")
    private BigDecimal receivableCommissionBalance;

    @ApiModelProperty(value = "应收手续费发生额")
    private BigDecimal receivableCommissionAmount;

    @ApiModelProperty(value = "应收返利余额")
    private BigDecimal receivableRebateBalance;

    @ApiModelProperty(value = "应收返利发生额")
    private BigDecimal receivableRebateAmount;

    @ApiModelProperty(value = "应收保险费余额")
    private BigDecimal receivableInsuranceBalance;

    @ApiModelProperty(value = "应收保险费发生额")
    private BigDecimal receivableInsuranceAmount;

    @ApiModelProperty(value = "应收其他收入余额")
    private BigDecimal receivableOtherincomeBalance;

    @ApiModelProperty(value = "应收其他收入发生额")
    private BigDecimal receivableOtherincomeAmount;

    @ApiModelProperty(value = "应收销项税余额")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "应收销项税发生额")
    private BigDecimal receivableOuttaxAmount;

    @ApiModelProperty(value = "应收销项税-本金余额")
    private BigDecimal receivableOutputtaxBaseBalance;

    @ApiModelProperty(value = "应收销项税-本金发生额")
    private BigDecimal receivableOutputtaxBaseAmount;

    @ApiModelProperty(value = "未确认收款余额")
    private BigDecimal receivableUnconfirmReceiptBalance;

    @ApiModelProperty(value = "未确认收款发生额")
    private BigDecimal receivableUnconfirmReceiptAmount;

    @ApiModelProperty(value = "应收服务费余额")
    private BigDecimal receivableServiceBalance;

    @ApiModelProperty(value = "应收服务费发生额")
    private BigDecimal receivableServiceAmount;

    @ApiModelProperty(value = "应收服务费-销项税余额")
    private BigDecimal receivableServiceOuttaxBalance;

    @ApiModelProperty(value = "应收服务费-销项税发生额")
    private BigDecimal receivableServiceOuttaxAmount;

    @ApiModelProperty(value = "未实现收益余额")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "未实现收益发生额")
    private BigDecimal unrealizedRevenueAmount;

    @ApiModelProperty(value = "融资租赁收益余额")
    private BigDecimal leaseRevenueBalance;

    @ApiModelProperty(value = "融资租赁收益发生额")
    private BigDecimal leaseRevenueAmount;

    @ApiModelProperty(value = "服务收入余额")
    private BigDecimal serviceRevenueBalance;

    @ApiModelProperty(value = "服务收入发生额")
    private BigDecimal serviceRevenueAmount;

    @ApiModelProperty(value = "保险费差额余额")
    private BigDecimal insuranceDifferBalance;

    @ApiModelProperty(value = "保险费差额发生额")
    private BigDecimal insuranceDifferAmount;

    @ApiModelProperty(value = "罚息收入余额")
    private BigDecimal dinterestRevenueBalance;

    @ApiModelProperty(value = "罚息收入发生额")
    private BigDecimal dinterestRevenueAmount;

    @ApiModelProperty(value = "合同解约及更改手续费余额")
    private BigDecimal terminateBalance;

    @ApiModelProperty(value = "合同解约及更改手续费发生额")
    private BigDecimal terminateAmount;

    @ApiModelProperty(value = "其他租赁相关收入余额")
    private BigDecimal otherRevenueBalance;

    @ApiModelProperty(value = "其他租赁相关收入发生额")
    private BigDecimal otherRevenueAmount;

    @ApiModelProperty(value = "违约金收入余额")
    private BigDecimal damagesRevenueBalance;

    @ApiModelProperty(value = "违约金收入发生额")
    private BigDecimal damagesRevenueAmount;

    @ApiModelProperty(value = "融资租赁业务保证金利息收入余额")
    private BigDecimal marginInterestBalance;

    @ApiModelProperty(value = "融资租赁业务保证金利息收入发生额")
    private BigDecimal marginInterestAmount;

    @ApiModelProperty(value = "应付租赁设备款-暂估余额")
    private BigDecimal payableDeviceEstimateBalance;

    @ApiModelProperty(value = "应付租赁设备款-暂估发生额")
    private BigDecimal payableDeviceEstimateAmount;

    @ApiModelProperty(value = "应付租赁设备款余额")
    private BigDecimal payableDeviceBalance;

    @ApiModelProperty(value = "应付租赁设备款发生额")
    private BigDecimal payableDeviceAmount;

    @ApiModelProperty(value = "应付其他租赁成本-暂估余额")
    private BigDecimal payableOtherCostEstimateBalance;

    @ApiModelProperty(value = "应付其他租赁成本-暂估发生额")
    private BigDecimal payableOtherCostEstimateAmount;

    @ApiModelProperty(value = "应付其他租赁成本余额")
    private BigDecimal payableOtherCostBalance;

    @ApiModelProperty(value = "应付其他租赁成本发生额")
    private BigDecimal payableOtherCostAmount;

    @ApiModelProperty(value = "应付经销商服务费-暂估余额")
    private BigDecimal payableAgencyEstimateBalance;

    @ApiModelProperty(value = "应付经销商服务费-暂估发生额")
    private BigDecimal payableAgencyEstimateAmount;

    @ApiModelProperty(value = "应付经销商服务费余额")
    private BigDecimal payableAgencyBalance;

    @ApiModelProperty(value = "应付经销商服务费发生额")
    private BigDecimal payableAgencyAmount;

    @ApiModelProperty(value = "应付收车费-暂估余额")
    private BigDecimal payableVehicleEstimateBalance;

    @ApiModelProperty(value = "应付收车费-暂估发生额")
    private BigDecimal payableVehicleEstimateAmount;

    @ApiModelProperty(value = "应付收车费余额")
    private BigDecimal payableVehicleBalance;

    @ApiModelProperty(value = "应付收车费发生额")
    private BigDecimal payableVehicleAmount;

    @ApiModelProperty(value = "应付手环成本_暂估余额")
    private BigDecimal payableBandCostEstimateBalance;

    @ApiModelProperty(value = "应付手环成本_暂估发生额")
    private BigDecimal payableBandCostEstimateAmount;

    @ApiModelProperty(value = "应付手环成本余额")
    private BigDecimal payableBandCostBalance;

    @ApiModelProperty(value = "应付手环成本发生额")
    private BigDecimal payableBandCostAmount;

    @ApiModelProperty(value = "应付抵押费_暂估余额")
    private BigDecimal payablePledgeEstimateBalance;

    @ApiModelProperty(value = "应付抵押费_暂估发生额")
    private BigDecimal payablePledgeEstimateAmount;

    @ApiModelProperty(value = "应付抵押费余额")
    private BigDecimal payablePledgeBalance;

    @ApiModelProperty(value = "应付抵押费发生额")
    private BigDecimal payablePledgeAmount;

    @ApiModelProperty(value = "应付解抵押费_暂估余额")
    private BigDecimal payableUnpledgeEstimateBalance;

    @ApiModelProperty(value = "应付解抵押费_暂估发生额")
    private BigDecimal payableUnpledgeEstimateAmount;

    @ApiModelProperty(value = "应付解抵押费余额")
    private BigDecimal payableUnpledgeBalance;

    @ApiModelProperty(value = "应付解抵押费发生额")
    private BigDecimal payableUnpledgeAmount;

    @ApiModelProperty(value = "承租人保证金余额")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "承租人保证金发生额")
    private BigDecimal lesseeMarginAmount;

    @ApiModelProperty(value = "供应商及代理商保证金余额")
    private BigDecimal supplierMarginBalance;

    @ApiModelProperty(value = "供应商及代理商保证金发生额")
    private BigDecimal supplierMarginAmount;

    @ApiModelProperty(value = "应付保险费-暂估余额")
    private BigDecimal payableInsuranceEstimateBalance;

    @ApiModelProperty(value = "应付保险费-暂估发生额")
    private BigDecimal payableInsuranceEstimateAmount;

    @ApiModelProperty(value = "应付保险费余额")
    private BigDecimal payableInsuranceBalance;

    @ApiModelProperty(value = "应付保险费发生额")
    private BigDecimal payableInsuranceAmount;

    @ApiModelProperty(value = "进项税额余额")
    private BigDecimal intaxBalance;

    @ApiModelProperty(value = "进项税额发生额")
    private BigDecimal intaxAmount;

    @ApiModelProperty(value = "销项税额余额")
    private BigDecimal outtaxBalance;

    @ApiModelProperty(value = "销项税额发生额")
    private BigDecimal outtaxAmount;

    @ApiModelProperty(value = "诉讼费余额")
    private BigDecimal litigationExpensesBalance;

    @ApiModelProperty(value = "诉讼费发生额")
    private BigDecimal litigationExpensesAmount;

    @ApiModelProperty(value = "减值准备余额")
    private BigDecimal depreciationReservesBalance;

    @ApiModelProperty(value = "减值准备发生额")
    private BigDecimal depreciationReservesAmount;

    @ApiModelProperty(value = "减值损失余额")
    private BigDecimal depreciationLossBalance;

    @ApiModelProperty(value = "减值损失发生额")
    private BigDecimal depreciationLossAmount;

    @ApiModelProperty(value = "回收融资租赁设备成本余额")
    private BigDecimal receiveCostBalance;

    @ApiModelProperty(value = "回收融资租赁设备成本发生额")
    private BigDecimal receiveCostAmount;

    @ApiModelProperty(value = "表外租赁收入余额")
    private BigDecimal offIncomeBalance;

    @ApiModelProperty(value = "表外租赁收入发生额")
    private BigDecimal offIncomeAmount;

    @ApiModelProperty(value = "递延收益余额")
    private BigDecimal deferIncomeBalance;

    @ApiModelProperty(value = "递延收益发生额")
    private BigDecimal deferIncomeAmount;

    @ApiModelProperty(value = "应付未付款余额")
    private BigDecimal payableAccountBalance;

    @ApiModelProperty(value = "应付未付款发生额")
    private BigDecimal payableAccountAmount;

    @ApiModelProperty(value = "应付保证金余额")
    private BigDecimal payableMarginBalance;

    @ApiModelProperty(value = "应付保证金发生额")
    private BigDecimal payableMarginAmount;

    @ApiModelProperty(value = "应付一年内保证金余额")
    private BigDecimal payableMarginYearBalance;

    @ApiModelProperty(value = "应付一年内保证金发生额")
    private BigDecimal payableMarginYearAmount;

    @ApiModelProperty(value = "保证金利息支出余额")
    private BigDecimal marginOinterestBalance;

    @ApiModelProperty(value = "保证金利息支出发生额")
    private BigDecimal marginOinterestAmount;

    @ApiModelProperty(value = "应收总额余额")
    private BigDecimal receiveSumBalance;

    @ApiModelProperty(value = "应收总额发生额")
    private BigDecimal receiveSumAmount;

    @ApiModelProperty(value = "应收总额_销项税余额")
    private BigDecimal receiveSumOuttaxBalance;

    @ApiModelProperty(value = "应收总额_销项税发生额")
    private BigDecimal receiveSumOuttaxAmount;

    @ApiModelProperty(value = "应收总额_未实现收益余额")
    private BigDecimal receiveUnrealizedRevenueBalance;

    @ApiModelProperty(value = "应收总额_未实现收益发生额")
    private BigDecimal receiveUnrealizedRevenueAmount;

    @ApiModelProperty(value = "应付一年内承租人保证金余额")
    private BigDecimal payableLesseeMarginYearBalance;

    @ApiModelProperty(value = "应付一年内承租人保证金发生额")
    private BigDecimal payableLesseeMarginYearAmount;

    @ApiModelProperty(value = "应付一年内供应商及代理商保证金余额")
    private BigDecimal payableSupplierMarginYearBalance;

    @ApiModelProperty(value = "应付一年内供应商及代理商保证金发生额")
    private BigDecimal payableSupplierMarginYearAmount;

    @ApiModelProperty(value = "应收诉讼费余额")
    private BigDecimal receivableLitigationExpensesBalance;

    @ApiModelProperty(value = "应收诉讼费发生额")
    private BigDecimal receivableLitigationExpensesAmount;

    @ApiModelProperty(value = "应付其他款项-暂估余额")
    private BigDecimal payableOtherEstimateBalance;

    @ApiModelProperty(value = "应付其他款项-暂估发生额")
    private BigDecimal payableOtherEstimateAmount;

    @ApiModelProperty(value = "应收贴息手续费余额")
    private BigDecimal payableDiscountCostBalance;

    @ApiModelProperty(value = "应收贴息手续费发生额")
    private BigDecimal payableDiscountCostAmount;

    @ApiModelProperty(value = "应付手续费成本余额")
    private BigDecimal payableProcedureCostBalance;

    @ApiModelProperty(value = "应付手续费成本发生额")
    private BigDecimal payableProcedureCostAmount;

    @ApiModelProperty(value = "暂收款项余额")
    private BigDecimal provisionalReceiptsBalance;

    @ApiModelProperty(value = "暂收款项发生额")
    private BigDecimal provisionalReceiptsAmount;

    @ApiModelProperty(value = "回收设备减值准备余额")
    private BigDecimal equipmentDepreciationReservesBalance;

    @ApiModelProperty(value = "回收设备减值准备发生额")
    private BigDecimal equipmentDepreciationReservesAmount;

    @ApiModelProperty(value = "回收设备减值损失余额")
    private BigDecimal equipmentDepreciationLossBalance;

    @ApiModelProperty(value = "回收设备减值损失发生额")
    private BigDecimal equipmentDepreciationLossAmount;

    @ApiModelProperty(value = "资产处置收益余额")
    private BigDecimal assetDisposeGainBalance;

    @ApiModelProperty(value = "资产处置收益发生额")
    private BigDecimal assetDisposeGainAmount;

    @ApiModelProperty(value = "资产处置损失余额")
    private BigDecimal assetDisposeLossBalance;

    @ApiModelProperty(value = "资产处置损失发生额")
    private BigDecimal assetDisposeLossAmount;

    @ApiModelProperty(value = "其他应收款余额")
    private BigDecimal receivableOtherBalance;

    @ApiModelProperty(value = "其他应收款发生额")
    private BigDecimal receivableOtherAmount;

    @ApiModelProperty(value = "应收转让后收款余额")
    private BigDecimal receivableCollectionTransferBalance;

    @ApiModelProperty(value = "应收转让后收款发生额")
    private BigDecimal receivableCollectionTransferAmount;

    @ApiModelProperty(value = "其他应收款_关联公司往来余额")
    private BigDecimal receivableRelatedPartyBalance;

    @ApiModelProperty(value = "其他应收款_关联公司往来发生额")
    private BigDecimal receivableRelatedPartyAmount;

    @ApiModelProperty(value = "代收转让款项余额")
    private BigDecimal collectionTransferBalance;

    @ApiModelProperty(value = "代收转让款项发生额")
    private BigDecimal collectionTransferAmount;

    @ApiModelProperty(value = "其他代收款余额")
    private BigDecimal collectPaymentOtherBalance;

    @ApiModelProperty(value = "其他代收款发生额")
    private BigDecimal collectPaymentOtherAmount;

    @ApiModelProperty(value = "应收利息余额")
    private BigDecimal receivableInterestBalance;

    @ApiModelProperty(value = "应收利息发生额")
    private BigDecimal receivableInterestAmount;

    @ApiModelProperty(value = "应收诉讼保证金余额")
    private BigDecimal receivableLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金发生额")
    private BigDecimal receivableLitigationMarginAmount;

    @ApiModelProperty(value = "房产_成本余额")
    private BigDecimal propertyCostBalance;

    @ApiModelProperty(value = "房产_成本发生额")
    private BigDecimal propertyCostAmount;

    @ApiModelProperty(value = "房产_减值准备 余额")
    private BigDecimal propertyDepreciationReservesBalance;

    @ApiModelProperty(value = "房产_减值准备 发生额")
    private BigDecimal propertyDepreciationReservesAmount;

    @ApiModelProperty(value = "机器设备_成本余额")
    private BigDecimal machineCostBalance;

    @ApiModelProperty(value = "机器设备_成本发生额")
    private BigDecimal machineCostAmount;

    @ApiModelProperty(value = "机器设备_减值准备余额")
    private BigDecimal machineDepreciationReservesBalance;

    @ApiModelProperty(value = "机器设备_减值准备发生额")
    private BigDecimal machineDepreciationReservesAmount;

    @ApiModelProperty(value = "其他_成本余额")
    private BigDecimal otherCostBalance;

    @ApiModelProperty(value = "其他_成本发生额")
    private BigDecimal otherCostAmount;

    @ApiModelProperty(value = "其他_减值准备余额")
    private BigDecimal otherDepreciationReservesBalance;

    @ApiModelProperty(value = "其他_减值准备发生额")
    private BigDecimal otherDepreciationReservesAmount;

    @ApiModelProperty(value = "应收保理本金余额")
    private BigDecimal receivableFactoringPrincipalBalance;

    @ApiModelProperty(value = "应收保理本金发生额")
    private BigDecimal receivableFactoringPrincipalAmount;

    @ApiModelProperty(value = "应收保理利息调整余额")
    private BigDecimal receivableFactoringInterestBalance;

    @ApiModelProperty(value = "应收保理利息调整发生额")
    private BigDecimal receivableFactoringInterestAmount;

    @ApiModelProperty(value = "应收贴息手续费_销项税余额")
    private BigDecimal payableDiscountCostOuttaxBalance;

    @ApiModelProperty(value = "应收贴息手续费_销项税发生额")
    private BigDecimal payableDiscountCostOuttaxAmount;

    @ApiModelProperty(value = "投资性房地产应收租金	余额")
    private BigDecimal receivableRentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金	发生额")
    private BigDecimal receivableRentInvestmentPropertyAmount;

    @ApiModelProperty(value = "投资性房地产应收销项税	余额")
    private BigDecimal receivableOuttaxInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收销项税	发生额")
    private BigDecimal receivableOuttaxInvestmentPropertyAmount;

    @ApiModelProperty(value = "应收贸易款坏账准备余额")
    private BigDecimal depreciationReservesTradeBalance;

    @ApiModelProperty(value = "应收贸易款坏账准备发生额")
    private BigDecimal depreciationReservesTradeAmount;

    @ApiModelProperty(value = "应收服务费坏账准备余额")
    private BigDecimal depreciationReservesServiceBalance;

    @ApiModelProperty(value = "应收服务费坏账准备发生额")
    private BigDecimal depreciationReservesServiceAmount;

    @ApiModelProperty(value = "其他应收款项坏账准备余额")
    private BigDecimal depreciationReservesOtherReceivableBalance;

    @ApiModelProperty(value = "其他应收款项坏账准备发生额")
    private BigDecimal depreciationReservesOtherReceivableAmount;

    @ApiModelProperty(value = "暂支及个人往来坏账准备余额")
    private BigDecimal depreciationReservesTempBalance;

    @ApiModelProperty(value = "暂支及个人往来坏账准备发生额")
    private BigDecimal depreciationReservesTempAmount;

    @ApiModelProperty(value = "其他保证金坏账准备余额")
    private BigDecimal depreciationReservesOtherMarginBalance;

    @ApiModelProperty(value = "其他保证金坏账准备发生额")
    private BigDecimal depreciationReservesOtherMarginAmount;

    @ApiModelProperty(value = "投资性房地产应收租金坏账准备余额")
    private BigDecimal depreciationReservesInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金坏账准备发生额")
    private BigDecimal depreciationReservesInvestmentPropertyAmount;

    @ApiModelProperty(value = "押金坏账准备余额")
    private BigDecimal depreciationReservesDepositBalance;

    @ApiModelProperty(value = "押金坏账准备发生额")
    private BigDecimal depreciationReservesDepositAmount;

    @ApiModelProperty(value = "应收诉讼保全费坏账准备余额")
    private BigDecimal depreciationReservesLitigationBalance;

    @ApiModelProperty(value = "应收诉讼保全费坏账准备发生额")
    private BigDecimal depreciationReservesLitigationAmount;

    @ApiModelProperty(value = "应收诉讼保证金坏账准备余额")
    private BigDecimal depreciationReservesLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金坏账准备发生额")
    private BigDecimal depreciationReservesLitigationMarginAmount;

    @ApiModelProperty(value = "其他坏账准备余额")
    private BigDecimal depreciationReservesOtherBalance;

    @ApiModelProperty(value = "其他坏账准备发生额")
    private BigDecimal depreciationReservesOtherAmount;

    @ApiModelProperty(value = "减值准备_单项余额")
    private BigDecimal depreciationReservesIndividualBalance;

    @ApiModelProperty(value = "减值准备_单项发生额")
    private BigDecimal depreciationReservesIndividualAmount;

    @ApiModelProperty(value = "应收票据坏账准备余额")
    private BigDecimal depreciationReservesBillBalance;

    @ApiModelProperty(value = "应收票据坏账准备发生额")
    private BigDecimal depreciationReservesBillAmount;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值准备余额")
    private BigDecimal depreciationReservesGovBalance;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值准备发生额")
    private BigDecimal depreciationReservesGovAmount;

    @ApiModelProperty(value = "其他长期应收款减值准备余额")
    private BigDecimal depreciationReservesOtherLongReceiblesBalance;

    @ApiModelProperty(value = "其他长期应收款减值准备发生额")
    private BigDecimal depreciationReservesOtherLongReceiblesAmount;

    @ApiModelProperty(value = "长期应收款关联公司往来减值准备余额")
    private BigDecimal depreciationReservesLongRelatedBalance;

    @ApiModelProperty(value = "长期应收款关联公司往来减值准备发生额")
    private BigDecimal depreciationReservesLongRelatedAmount;

    @ApiModelProperty(value = "借款应收利息减值准备余额")
    private BigDecimal depreciationReservesInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值准备发生额")
    private BigDecimal depreciationReservesInterestAmount;

    @ApiModelProperty(value = "本金余额")
    private BigDecimal receivablePrincipalBalance;

    @ApiModelProperty(value = "本金发生额")
    private BigDecimal receivablePrincipalAmount;

    @ApiModelProperty(value = "利息调整余额")
    private BigDecimal receivableInterestAdjustmentBalance;

    @ApiModelProperty(value = "利息调整发生额")
    private BigDecimal receivableInterestAdjustmentAmount;

    @ApiModelProperty(value = "贷款_非金融机构_本金余额")
    private BigDecimal receivablePrincipalNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_本金发生额")
    private BigDecimal receivablePrincipalNonfinancialAmount;

    @ApiModelProperty(value = "贷款_非金融机构_应收手续费余额")
    private BigDecimal receivableCommissionNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_应收手续费发生额")
    private BigDecimal receivableCommissionNonfinancialAmount;

    @ApiModelProperty(value = "贷款_非金融机构_利息调整余额")
    private BigDecimal receivableInterestAdjustmentNonfinancialBalance;

    @ApiModelProperty(value = "贷款_非金融机构_利息调整发生额")
    private BigDecimal receivableInterestAdjustmentNonfinancialAmount;

    @ApiModelProperty(value = "银行存款减值准备余额")
    private BigDecimal depreciationReservesBankBalance;

    @ApiModelProperty(value = "银行存款减值准备发生额")
    private BigDecimal depreciationReservesBankAmount;

    @ApiModelProperty(value = "买入返售金融资产减值准备余额")
    private BigDecimal depreciationReservesBuyingBackBalance;

    @ApiModelProperty(value = "买入返售金融资产减值准备发生额")
    private BigDecimal depreciationReservesBuyingBackAmount;

    @ApiModelProperty(value = "定期存款应收利息减值准备余额")
    private BigDecimal depreciationReservesTermDepositInterestBalance;

    @ApiModelProperty(value = "定期存款应收利息减值准备发生额")
    private BigDecimal depreciationReservesTermDepositInterestAmount;

    @ApiModelProperty(value = "借款应收利息减值准备余额")
    private BigDecimal depreciationReservesBorrowingsInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值准备发生额")
    private BigDecimal depreciationReservesBorrowingsInterestAmount;

    @ApiModelProperty(value = "买入返售金融资产应收利息减值准备余额")
    private BigDecimal depreciationReservesBuyingBackInterestBalance;

    @ApiModelProperty(value = "买入返售金融资产应收利息减值准备发生额")
    private BigDecimal depreciationReservesBuyingBackInterestAmount;

    @ApiModelProperty(value = "银行理财产品应收利息减值准备余额")
    private BigDecimal depreciationReservesFinancialProductInterestBalance;

    @ApiModelProperty(value = "银行理财产品应收利息减值准备发生额")
    private BigDecimal depreciationReservesFinancialProductInterestAmount;

    @ApiModelProperty(value = "结构性存款应收利息减值准备余额")
    private BigDecimal depreciationReservesStructuredDepositInterestBalance;

    @ApiModelProperty(value = "结构性存款应收利息减值准备发生额")
    private BigDecimal depreciationReservesStructuredDepositInterestAmount;

    @ApiModelProperty(value = "FVPL_应收利息减值准备余额")
    private BigDecimal depreciationReservesFvplInterestOtherBalance;

    @ApiModelProperty(value = "FVPL_应收利息减值准备发生额")
    private BigDecimal depreciationReservesFvplInterestOtherAmount;

    @ApiModelProperty(value = "FVOCI_应收利息减值准备余额")
    private BigDecimal depreciationReservesBondFvociInterestBalance;

    @ApiModelProperty(value = "FVOCI_应收利息减值准备发生额")
    private BigDecimal depreciationReservesBondFvociInterestAmount;

    @ApiModelProperty(value = "FVOCI_其他应收利息减值准备余额")
    private BigDecimal depreciationReservesFvociInterestOtherBalance;

    @ApiModelProperty(value = "FVOCI_其他应收利息减值准备发生额")
    private BigDecimal depreciationReservesFvociInterestOtherAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券应收利息减值准备余额")
    private BigDecimal depreciationReservesBondAcInterestBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券应收利息减值准备发生额")
    private BigDecimal depreciationReservesBondAcInterestAmount;

    @ApiModelProperty(value = "其他以摊余成本计量的金融资产应收利息减值准备余额")
    private BigDecimal depreciationReservesAcInterestOtherBalance;

    @ApiModelProperty(value = "其他以摊余成本计量的金融资产应收利息减值准备发生额")
    private BigDecimal depreciationReservesAcInterestOtherAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券减值准备余额")
    private BigDecimal depreciationReservesBondAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券减值准备发生额")
    private BigDecimal depreciationReservesBondAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值准备余额")
    private BigDecimal depreciationReservesTurstAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值准备发生额")
    private BigDecimal depreciationReservesTurstAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值准备余额")
    private BigDecimal depreciationReservesOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值准备发生额")
    private BigDecimal depreciationReservesOtherFinancialAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值准备余额")
    private BigDecimal depreciationReservesOtherAssetAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值准备发生额")
    private BigDecimal depreciationReservesOtherAssetAcAmount;

    @ApiModelProperty(value = "FVOCI_债券减值准备余额")
    private BigDecimal depreciationReservesBondFvociBalance;

    @ApiModelProperty(value = "FVOCI_债券减值准备发生额")
    private BigDecimal depreciationReservesBondFvociAmount;

    @ApiModelProperty(value = "FVOCI_其他金融资产减值准备余额")
    private BigDecimal depreciationReservesOtherFinancialFvociBalance;

    @ApiModelProperty(value = "FVOCI_其他金融资产减值准备发生额")
    private BigDecimal depreciationReservesOtherFinancialFvociAmount;

    @ApiModelProperty(value = "投资子公司减值准备余额")
    private BigDecimal depreciationReservesSubsidiaryBalance;

    @ApiModelProperty(value = "投资子公司减值准备发生额")
    private BigDecimal depreciationReservesSubsidiaryAmount;

    @ApiModelProperty(value = "投资合营企业减值准备余额")
    private BigDecimal depreciationReservesJvBalance;

    @ApiModelProperty(value = "投资合营企业减值准备发生额")
    private BigDecimal depreciationReservesJvAmount;

    @ApiModelProperty(value = "投资联营企业减值准备余额")
    private BigDecimal depreciationReservesAssociateBalance;

    @ApiModelProperty(value = "投资联营企业减值准备发生额")
    private BigDecimal depreciationReservesAssociateAmount;

    @ApiModelProperty(value = "虚拟收付款余额")
    private BigDecimal receivableVirtualBalance;

    @ApiModelProperty(value = "虚拟收付款发生额")
    private BigDecimal receivableVirtualAmount;

    @ApiModelProperty(value = "未实现其他收益余额")
    private BigDecimal unrealizedRevenueOtherBalance;

    @ApiModelProperty(value = "未实现其他收益发生额")
    private BigDecimal unrealizedRevenueOtherAmount;

    @ApiModelProperty(value = "应付保理款余额")
    private BigDecimal payableFactoringBalance;

    @ApiModelProperty(value = "应付保理款发生额")
    private BigDecimal payableFactoringAmount;

    @ApiModelProperty(value = "应付委贷款余额")
    private BigDecimal payableEntrustBalance;

    @ApiModelProperty(value = "应付委贷款发生额")
    private BigDecimal payableEntrustAmount;

    @ApiModelProperty(value = "代收理赔款余额")
    private BigDecimal collectClaimsBalance;

    @ApiModelProperty(value = "代收理赔款发生额")
    private BigDecimal collectClaimsAmount;

    @ApiModelProperty(value = "应付其他款项余额")
    private BigDecimal payableOtherBalance;

    @ApiModelProperty(value = "应付其他款项发生额")
    private BigDecimal payableOtherAmount;

    @ApiModelProperty(value = "预收租赁款余额")
    private BigDecimal prereceivedRentBalance;

    @ApiModelProperty(value = "预收租赁款发生额")
    private BigDecimal prereceivedRentAmount;

    @ApiModelProperty(value = "其他应付款_关联公司往来	余额")
    private BigDecimal payableRelatedPartyBalance;

    @ApiModelProperty(value = "其他应付款_关联公司往来	发生额")
    private BigDecimal payableRelatedPartyAmount;

    @ApiModelProperty(value = "其他应付款_资产支持专项计划余额")
    private BigDecimal otherPayableSpvBalance;

    @ApiModelProperty(value = "其他应付款_资产支持专项计划发生额")
    private BigDecimal otherPayableSpvAmount;

    @ApiModelProperty(value = "其他应付款_信托计划余额")
    private BigDecimal otherPayableTrustBalance;

    @ApiModelProperty(value = "其他应付款_信托计划发生额")
    private BigDecimal otherPayableTrustAmount;

    @ApiModelProperty(value = "其他应付款_出表保理资产余额")
    private BigDecimal otherPayableClaimAssetBalance;

    @ApiModelProperty(value = "其他应付款_出表保理资产发生额")
    private BigDecimal otherPayableClaimAssetAmount;

    @ApiModelProperty(value = "其他应付款_租金余额余额")
    private BigDecimal otherPayableRentBalance;

    @ApiModelProperty(value = "其他应付款_租金余额发生额")
    private BigDecimal otherPayableRentAmount;

    @ApiModelProperty(value = "其他应付款_残值余额余额")
    private BigDecimal otherPayableResidualBalance;

    @ApiModelProperty(value = "其他应付款_残值余额发生额")
    private BigDecimal otherPayableResidualAmount;

    @ApiModelProperty(value = "其他应付款_其他余额")
    private BigDecimal otherPayableOtherBalance;

    @ApiModelProperty(value = "其他应付款_其他发生额")
    private BigDecimal otherPayableOtherAmount;

    @ApiModelProperty(value = "其他应付款_保理款余额余额")
    private BigDecimal otherPayableClaimBalance;

    @ApiModelProperty(value = "其他应付款_保理款余额发生额")
    private BigDecimal otherPayableClaimAmount;

    @ApiModelProperty(value = "其他应付款_保理款余额_其他余额")
    private BigDecimal otherPayableClaimOtherBalance;

    @ApiModelProperty(value = "其他应付款_保理款余额_其他发生额")
    private BigDecimal otherPayableClaimOtherAmount;

    @ApiModelProperty(value = "其他应付款_代收出表保理资产款项余额")
    private BigDecimal otherPayableCollectBalance;

    @ApiModelProperty(value = "其他应付款_代收出表保理资产款项发生额")
    private BigDecimal otherPayableCollectAmount;

    @ApiModelProperty(value = "供应商资金池余额")
    private BigDecimal supplierPoolBalance;

    @ApiModelProperty(value = "供应商资金池发生额")
    private BigDecimal supplierPoolAmount;

    @ApiModelProperty(value = "代理商保证金余额")
    private BigDecimal agentMarginBalance;

    @ApiModelProperty(value = "代理商保证金发生额")
    private BigDecimal agentMarginAmount;

    @ApiModelProperty(value = "其他保证金余额")
    private BigDecimal otherMarginBalance;

    @ApiModelProperty(value = "其他保证金发生额")
    private BigDecimal otherMarginAmount;

    @ApiModelProperty(value = "房屋租赁保证金余额")
    private BigDecimal rentMarginBalance;

    @ApiModelProperty(value = "房屋租赁保证金发生额")
    private BigDecimal rentMarginAmount;

    @ApiModelProperty(value = "其他长期应付保证金余额")
    private BigDecimal otherLongMarginBalance;

    @ApiModelProperty(value = "其他长期应付保证金发生额")
    private BigDecimal otherLongMarginAmount;

    @ApiModelProperty(value = "其他主营业务收入	余额")
    private BigDecimal otherBusinessIncomeBalance;

    @ApiModelProperty(value = "其他主营业务收入	发生额")
    private BigDecimal otherBusinessIncomeAmount;

    @ApiModelProperty(value = "其他主营业务收入_手续费收入余额")
    private BigDecimal otherIncomeServiceBalance;

    @ApiModelProperty(value = "其他主营业务收入_手续费收入发生额")
    private BigDecimal otherIncomeServiceAmount;

    @ApiModelProperty(value = "其他长期应收款利息收入余额")
    private BigDecimal interestOtherLongPayablesBalance;

    @ApiModelProperty(value = "其他长期应收款利息收入发生额")
    private BigDecimal interestOtherLongPayablesAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产利息收入余额")
    private BigDecimal interestOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产利息收入发生额")
    private BigDecimal interestOtherFinancialAcAmount;

    @ApiModelProperty(value = "租赁收益6%余额")
    private BigDecimal leaseRevenue6Balance;

    @ApiModelProperty(value = "租赁收益6%发生额")
    private BigDecimal leaseRevenue6Amount;

    @ApiModelProperty(value = "租赁收益3%余额")
    private BigDecimal leaseRevenue3Balance;

    @ApiModelProperty(value = "租赁收益3%发生额")
    private BigDecimal leaseRevenue3Amount;

    @ApiModelProperty(value = "其他业务收入_融资租赁款转让收益余额")
    private BigDecimal otherIncomeLeaseTransferBalance;

    @ApiModelProperty(value = "其他业务收入_融资租赁款转让收益发生额")
    private BigDecimal otherIncomeLeaseTransferAmount;

    @ApiModelProperty(value = "投资性房地产租金收入余额")
    private BigDecimal rentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产租金收入发生额")
    private BigDecimal rentInvestmentPropertyAmount;

    @ApiModelProperty(value = "其他业务收入_应收保理款转让收益余额")
    private BigDecimal otherIncomeFactoringTransferBalance;

    @ApiModelProperty(value = "其他业务收入_应收保理款转让收益发生额")
    private BigDecimal otherIncomeFactoringTransferAmount;

    @ApiModelProperty(value = "抵债资产处置收益余额")
    private BigDecimal assetDisposeGainForeclosedBalance;

    @ApiModelProperty(value = "抵债资产处置收益发生额")
    private BigDecimal assetDisposeGainForeclosedAmount;

    @ApiModelProperty(value = "抵债资产处置损失余额")
    private BigDecimal assetDisposeLossForeclosedBalance;

    @ApiModelProperty(value = "抵债资产处置损失发生额")
    private BigDecimal assetDisposeLossForeclosedAmount;

    @ApiModelProperty(value = "其他业务成本_融资租赁款转让收益余额")
    private BigDecimal otherCostLeaseTransferBalance;

    @ApiModelProperty(value = "其他业务成本_融资租赁款转让收益发生额")
    private BigDecimal otherCostLeaseTransferAmount;

    @ApiModelProperty(value = "其他业务成本_应收保理款转让收益余额")
    private BigDecimal otherCostFactoringTransferBalance;

    @ApiModelProperty(value = "其他业务成本_应收保理款转让收益发生额")
    private BigDecimal otherCostFactoringTransferAmount;

    @ApiModelProperty(value = "评估费余额")
    private BigDecimal assessmentFeeBalance;

    @ApiModelProperty(value = "评估费发生额")
    private BigDecimal assessmentFeeAmount;

    @ApiModelProperty(value = "律师费余额")
    private BigDecimal attorneyFeeBalance;

    @ApiModelProperty(value = "律师费发生额")
    private BigDecimal attorneyFeeAmount;

    @ApiModelProperty(value = "其他聘请中介机构费余额")
    private BigDecimal intermediaryFeeOtherBalance;

    @ApiModelProperty(value = "其他聘请中介机构费发生额")
    private BigDecimal intermediaryFeeOtherAmount;

    @ApiModelProperty(value = "咨询费余额")
    private BigDecimal consultationFeeBalance;

    @ApiModelProperty(value = "咨询费发生额")
    private BigDecimal consultationFeeAmount;

    @ApiModelProperty(value = "公证费余额")
    private BigDecimal notaryFeeBalance;

    @ApiModelProperty(value = "公证费发生额")
    private BigDecimal notaryFeeAmount;

    @ApiModelProperty(value = "回收租赁资产杂费余额")
    private BigDecimal leaseAssetRecoveryFeeBalance;

    @ApiModelProperty(value = "回收租赁资产杂费发生额")
    private BigDecimal leaseAssetRecoveryFeeAmount;

    @ApiModelProperty(value = "应收贸易款坏账损失余额")
    private BigDecimal depreciationLossTradeBalance;

    @ApiModelProperty(value = "应收贸易款坏账损失发生额")
    private BigDecimal depreciationLossTradeAmount;

    @ApiModelProperty(value = "应收服务费坏账损失余额")
    private BigDecimal depreciationLossServiceBalance;

    @ApiModelProperty(value = "应收服务费坏账损失发生额")
    private BigDecimal depreciationLossServiceAmount;

    @ApiModelProperty(value = "其他应收款项坏账损失余额")
    private BigDecimal depreciationLossOtherReceivablesBalance;

    @ApiModelProperty(value = "其他应收款项坏账损失发生额")
    private BigDecimal depreciationLossOtherReceivablesAmount;

    @ApiModelProperty(value = "暂支及个人往来坏账损失余额")
    private BigDecimal depreciationLossTempBalance;

    @ApiModelProperty(value = "暂支及个人往来坏账损失发生额")
    private BigDecimal depreciationLossTempAmount;

    @ApiModelProperty(value = "其他保证金坏账损失余额")
    private BigDecimal depreciationLossOtherMarginBalance;

    @ApiModelProperty(value = "其他保证金坏账损失发生额")
    private BigDecimal depreciationLossOtherMarginAmount;

    @ApiModelProperty(value = "投资性房地产应收租金减值损失余额")
    private BigDecimal depreciationLossRentInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产应收租金减值损失发生额")
    private BigDecimal depreciationLossRentInvestmentPropertyAmount;

    @ApiModelProperty(value = "押金减值损失余额")
    private BigDecimal depreciationLossDepositBalance;

    @ApiModelProperty(value = "押金减值损失发生额")
    private BigDecimal depreciationLossDepositAmount;

    @ApiModelProperty(value = "应收诉讼保全费减值损失余额")
    private BigDecimal depreciationLossLitigationBalance;

    @ApiModelProperty(value = "应收诉讼保全费减值损失发生额")
    private BigDecimal depreciationLossLitigationAmount;

    @ApiModelProperty(value = "应收诉讼保证金减值损失余额")
    private BigDecimal depreciationLossLitigationMarginBalance;

    @ApiModelProperty(value = "应收诉讼保证金减值损失发生额")
    private BigDecimal depreciationLossLitigationMarginAmount;

    @ApiModelProperty(value = "减值损失_其他余额")
    private BigDecimal depreciationLossOtherReceivableBalance;

    @ApiModelProperty(value = "减值损失_其他发生额")
    private BigDecimal depreciationLossOtherReceivableAmount;

    @ApiModelProperty(value = "应收融资租赁款减值损失_坏账注销转回余额")
    private BigDecimal depreciationLossReverseBalance;

    @ApiModelProperty(value = "应收融资租赁款减值损失_坏账注销转回发生额")
    private BigDecimal depreciationLossReverseAmount;

    @ApiModelProperty(value = "长期股权投资减值损失余额")
    private BigDecimal depreciationLossEquityInvestmentBalance;

    @ApiModelProperty(value = "长期股权投资减值损失发生额")
    private BigDecimal depreciationLossEquityInvestmentAmount;

    @ApiModelProperty(value = "应收利息减值损失余额")
    private BigDecimal depreciationLossInterestBalance;

    @ApiModelProperty(value = "应收利息减值损失发生额")
    private BigDecimal depreciationLossInterestAmount;

    @ApiModelProperty(value = "应收票据减值损失余额")
    private BigDecimal depreciationLossBillBalance;

    @ApiModelProperty(value = "应收票据减值损失发生额")
    private BigDecimal depreciationLossBillAmount;

    @ApiModelProperty(value = "抵债资产减值损失余额")
    private BigDecimal depreciationLossCollateralBalance;

    @ApiModelProperty(value = "抵债资产减值损失发生额")
    private BigDecimal depreciationLossCollateralAmount;

    @ApiModelProperty(value = "金融资产减值损失余额")
    private BigDecimal depreciationLossFinancialBalance;

    @ApiModelProperty(value = "金融资产减值损失发生额")
    private BigDecimal depreciationLossFinancialAmount;

    @ApiModelProperty(value = "买入返售金融资产减值损失余额")
    private BigDecimal depreciationLossBuyingBackBalance;

    @ApiModelProperty(value = "买入返售金融资产减值损失发生额")
    private BigDecimal depreciationLossBuyingBackAmount;

    @ApiModelProperty(value = "以摊余成本计量的债券减值损失余额")
    private BigDecimal depreciationLossBondAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的债券减值损失发生额")
    private BigDecimal depreciationLossBondAcAmount;

    @ApiModelProperty(value = "FVOCI_金融资产减值损失余额")
    private BigDecimal depreciationLossFinancialFvociBalance;

    @ApiModelProperty(value = "FVOCI_金融资产减值损失发生额")
    private BigDecimal depreciationLossFinancialFvociAmount;

    @ApiModelProperty(value = "银行存款减值损失余额")
    private BigDecimal depreciationLossBankBalance;

    @ApiModelProperty(value = "银行存款减值损失发生额")
    private BigDecimal depreciationLossBankAmount;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值损失余额")
    private BigDecimal depreciationLossTrustAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的信托计划减值损失发生额")
    private BigDecimal depreciationLossTrustAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值损失余额")
    private BigDecimal depreciationLossOtherAssetAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他资产减值损失发生额")
    private BigDecimal depreciationLossOtherAssetAcAmount;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值损失余额")
    private BigDecimal depreciationLossOtherFinancialAcBalance;

    @ApiModelProperty(value = "以摊余成本计量的其他金融资产减值损失发生额")
    private BigDecimal depreciationLossOtherFinancialAcAmount;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值损失余额")
    private BigDecimal depreciationLossGovBalance;

    @ApiModelProperty(value = "长期应收政府与社会资本合作项目减值损失发生额")
    private BigDecimal depreciationLossGovAmount;

    @ApiModelProperty(value = "其他长期应收款减值损失余额")
    private BigDecimal depreciationLossOtherLongReceivablesBalance;

    @ApiModelProperty(value = "其他长期应收款减值损失发生额")
    private BigDecimal depreciationLossOtherLongReceivablesAmount;

    @ApiModelProperty(value = "长期应收款关联公司往来减值损失余额")
    private BigDecimal depreciationLossOtherLongRelatedBalance;

    @ApiModelProperty(value = "长期应收款关联公司往来减值损失发生额")
    private BigDecimal depreciationLossOtherLongRelatedAmount;

    @ApiModelProperty(value = "借款应收利息减值损失余额")
    private BigDecimal depreciationLossBorrowingsInterestBalance;

    @ApiModelProperty(value = "借款应收利息减值损失发生额")
    private BigDecimal depreciationLossBorrowingsInterestAmount;

    @ApiModelProperty(value = "投资性房地产减值损失余额")
    private BigDecimal depreciationLossInvestmentPropertyBalance;

    @ApiModelProperty(value = "投资性房地产减值损失发生额")
    private BigDecimal depreciationLossInvestmentPropertyAmount;

    @ApiModelProperty(value = "其他减值损失余额")
    private BigDecimal depreciationLossOtherBalance;

    @ApiModelProperty(value = "其他减值损失发生额")
    private BigDecimal depreciationLossOtherAmount;

    @ApiModelProperty(value = "公告费余额")
    private BigDecimal publicationFeeBalance;

    @ApiModelProperty(value = "公告费发生额")
    private BigDecimal publicationFeeAmount;

    @ApiModelProperty(value = "债务重组项目应收总额	余额")
    private BigDecimal receiveSumDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目应收总额	发生额")
    private BigDecimal receiveSumDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目未实现收益余额")
    private BigDecimal unrealizedRevenueDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目未实现收益发生额")
    private BigDecimal unrealizedRevenueDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目应收销项税余额")
    private BigDecimal receivableOuttaxDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目应收销项税发生额")
    private BigDecimal receivableOuttaxDebtRestructureAmount;

    @ApiModelProperty(value = "债务重组项目未实现其他收益余额")
    private BigDecimal unrealizedRevenueOtherDebtRestructureBalance;

    @ApiModelProperty(value = "债务重组项目未实现其他收益发生额")
    private BigDecimal unrealizedRevenueOtherDebtRestructureAmount;

}
