package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
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
 * 最新的余额表数据实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_balance_latest")
public class ContractBalanceLatestEntity extends Model<ContractBalanceLatestEntity> {

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
    @TableLogic
    private String delFlag;
    //应收租金余额
    private BigDecimal receivableRentBalance;

    //应收租金发生额
    private BigDecimal receivableRentAmount;

    //应收首付款余额
    private BigDecimal receivableDownpaymentBalance;

    //应收首付款发生额
    private BigDecimal receivableDownpaymentAmount;

    //应收期末残值余额
    private BigDecimal receivableResidualValueBalance;

    //应收期末残值发生额
    private BigDecimal receivableResidualValueAmount;

    //应收手续费余额
    private BigDecimal receivableCommissionBalance;

    //应收手续费发生额
    private BigDecimal receivableCommissionAmount;

    //应收返利余额
    private BigDecimal receivableRebateBalance;

    //应收返利发生额
    private BigDecimal receivableRebateAmount;

    //应收保险费余额
    private BigDecimal receivableInsuranceBalance;

    //应收保险费发生额
    private BigDecimal receivableInsuranceAmount;

    //应收其他收入余额
    private BigDecimal receivableOtherincomeBalance;

    //应收其他收入发生额
    private BigDecimal receivableOtherincomeAmount;

    //应收销项税余额
    private BigDecimal receivableOuttaxBalance;

    //应收销项税发生额
    private BigDecimal receivableOuttaxAmount;

    //应收销项税-本金余额
    private BigDecimal receivableOutputtaxBaseBalance;

    //应收销项税-本金发生额
    private BigDecimal receivableOutputtaxBaseAmount;

    //未确认收款余额
    private BigDecimal receivableUnconfirmReceiptBalance;

    //未确认收款发生额
    private BigDecimal receivableUnconfirmReceiptAmount;

    //应收服务费余额
    private BigDecimal receivableServiceBalance;

    //应收服务费发生额
    private BigDecimal receivableServiceAmount;

    //应收服务费-销项税余额
    private BigDecimal receivableServiceOuttaxBalance;

    //应收服务费-销项税发生额
    private BigDecimal receivableServiceOuttaxAmount;

    //未实现收益余额
    private BigDecimal unrealizedRevenueBalance;

    //未实现收益发生额
    private BigDecimal unrealizedRevenueAmount;

    //融资租赁收益余额
    private BigDecimal leaseRevenueBalance;

    //融资租赁收益发生额
    private BigDecimal leaseRevenueAmount;

    //服务收入余额
    private BigDecimal serviceRevenueBalance;

    //服务收入发生额
    private BigDecimal serviceRevenueAmount;

    //保险费差额余额
    private BigDecimal insuranceDifferBalance;

    //保险费差额发生额
    private BigDecimal insuranceDifferAmount;

    //罚息收入余额
    private BigDecimal dinterestRevenueBalance;

    //罚息收入发生额
    private BigDecimal dinterestRevenueAmount;

    //合同解约及更改手续费余额
    private BigDecimal terminateBalance;

    //合同解约及更改手续费发生额
    private BigDecimal terminateAmount;

    //其他租赁相关收入余额
    private BigDecimal otherRevenueBalance;

    //其他租赁相关收入发生额
    private BigDecimal otherRevenueAmount;

    //违约金收入余额
    private BigDecimal damagesRevenueBalance;

    //违约金收入发生额
    private BigDecimal damagesRevenueAmount;

    //融资租赁业务保证金利息收入余额
    private BigDecimal marginInterestBalance;

    //融资租赁业务保证金利息收入发生额
    private BigDecimal marginInterestAmount;

    //应付租赁设备款-暂估余额
    private BigDecimal payableDeviceEstimateBalance;

    //应付租赁设备款-暂估发生额
    private BigDecimal payableDeviceEstimateAmount;

    //应付租赁设备款余额
    private BigDecimal payableDeviceBalance;

    //应付租赁设备款发生额
    private BigDecimal payableDeviceAmount;

    //应付其他租赁成本-暂估余额
    private BigDecimal payableOtherCostEstimateBalance;

    //应付其他租赁成本-暂估发生额
    private BigDecimal payableOtherCostEstimateAmount;

    //应付其他租赁成本余额
    private BigDecimal payableOtherCostBalance;

    //应付其他租赁成本发生额
    private BigDecimal payableOtherCostAmount;

    //应付经销商服务费-暂估余额
    private BigDecimal payableAgencyEstimateBalance;

    //应付经销商服务费-暂估发生额
    private BigDecimal payableAgencyEstimateAmount;

    //应付经销商服务费余额
    private BigDecimal payableAgencyBalance;

    //应付经销商服务费发生额
    private BigDecimal payableAgencyAmount;

    //应付收车费-暂估余额
    private BigDecimal payableVehicleEstimateBalance;

    //应付收车费-暂估发生额
    private BigDecimal payableVehicleEstimateAmount;

    //应付收车费余额
    private BigDecimal payableVehicleBalance;

    //应付收车费发生额
    private BigDecimal payableVehicleAmount;

    //应付手环成本_暂估余额
    private BigDecimal payableBandCostEstimateBalance;

    //应付手环成本_暂估发生额
    private BigDecimal payableBandCostEstimateAmount;

    //应付手环成本余额
    private BigDecimal payableBandCostBalance;

    //应付手环成本发生额
    private BigDecimal payableBandCostAmount;

    //应付抵押费_暂估余额
    private BigDecimal payablePledgeEstimateBalance;

    //应付抵押费_暂估发生额
    private BigDecimal payablePledgeEstimateAmount;

    //应付抵押费余额
    private BigDecimal payablePledgeBalance;

    //应付抵押费发生额
    private BigDecimal payablePledgeAmount;

    //应付解抵押费_暂估余额
    private BigDecimal payableUnpledgeEstimateBalance;

    //应付解抵押费_暂估发生额
    private BigDecimal payableUnpledgeEstimateAmount;

    //应付解抵押费余额
    private BigDecimal payableUnpledgeBalance;

    //应付解抵押费发生额
    private BigDecimal payableUnpledgeAmount;

    //承租人保证金余额
    private BigDecimal lesseeMarginBalance;

    //承租人保证金发生额
    private BigDecimal lesseeMarginAmount;

    //供应商及代理商保证金余额
    private BigDecimal supplierMarginBalance;

    //供应商及代理商保证金发生额
    private BigDecimal supplierMarginAmount;

    //应付保险费-暂估余额
    private BigDecimal payableInsuranceEstimateBalance;

    //应付保险费-暂估发生额
    private BigDecimal payableInsuranceEstimateAmount;

    //应付保险费余额
    private BigDecimal payableInsuranceBalance;

    //应付保险费发生额
    private BigDecimal payableInsuranceAmount;

    //进项税额余额
    private BigDecimal intaxBalance;

    //进项税额发生额
    private BigDecimal intaxAmount;

    //销项税额余额
    private BigDecimal outtaxBalance;

    //销项税额发生额
    private BigDecimal outtaxAmount;

    //诉讼费余额
    private BigDecimal litigationExpensesBalance;

    //诉讼费发生额
    private BigDecimal litigationExpensesAmount;

    //减值准备余额
    private BigDecimal depreciationReservesBalance;

    //减值准备发生额
    private BigDecimal depreciationReservesAmount;

    //减值损失余额
    private BigDecimal depreciationLossBalance;

    //减值损失发生额
    private BigDecimal depreciationLossAmount;

    //回收融资租赁设备成本余额
    private BigDecimal receiveCostBalance;

    //回收融资租赁设备成本发生额
    private BigDecimal receiveCostAmount;

    //表外租赁收入余额
    private BigDecimal offIncomeBalance;

    //表外租赁收入发生额
    private BigDecimal offIncomeAmount;

    //递延收益余额
    private BigDecimal deferIncomeBalance;

    //递延收益发生额
    private BigDecimal deferIncomeAmount;

    //应付未付款余额
    private BigDecimal payableAccountBalance;

    //应付未付款发生额
    private BigDecimal payableAccountAmount;

    //应付保证金余额
    private BigDecimal payableMarginBalance;

    //应付保证金发生额
    private BigDecimal payableMarginAmount;

    //应付一年内保证金余额
    private BigDecimal payableMarginYearBalance;

    //应付一年内保证金发生额
    private BigDecimal payableMarginYearAmount;

    //保证金利息支出余额
    private BigDecimal marginOinterestBalance;

    //保证金利息支出发生额
    private BigDecimal marginOinterestAmount;

    //应收总额余额
    private BigDecimal receiveSumBalance;

    //应收总额发生额
    private BigDecimal receiveSumAmount;

    //应收总额_销项税余额
    private BigDecimal receiveSumOuttaxBalance;

    //应收总额_销项税发生额
    private BigDecimal receiveSumOuttaxAmount;

    //应收总额_未实现收益余额
    private BigDecimal receiveUnrealizedRevenueBalance;

    //应收总额_未实现收益发生额
    private BigDecimal receiveUnrealizedRevenueAmount;

    //应付一年内承租人保证金余额
    private BigDecimal payableLesseeMarginYearBalance;

    //应付一年内承租人保证金发生额
    private BigDecimal payableLesseeMarginYearAmount;

    //应付一年内供应商及代理商保证金余额
    private BigDecimal payableSupplierMarginYearBalance;

    //应付一年内供应商及代理商保证金发生额
    private BigDecimal payableSupplierMarginYearAmount;

    //应收诉讼费余额
    private BigDecimal receivableLitigationExpensesBalance;

    //应收诉讼费发生额
    private BigDecimal receivableLitigationExpensesAmount;

    //应付其他款项-暂估余额
    private BigDecimal payableOtherEstimateBalance;

    //应付其他款项-暂估发生额
    private BigDecimal payableOtherEstimateAmount;

    //应收贴息手续费余额
    private BigDecimal payableDiscountCostBalance;

    //应收贴息手续费发生额
    private BigDecimal payableDiscountCostAmount;

    //应付手续费成本余额
    private BigDecimal payableProcedureCostBalance;

    //应付手续费成本发生额
    private BigDecimal payableProcedureCostAmount;

    //暂收款项余额
    private BigDecimal provisionalReceiptsBalance;

    //暂收款项发生额
    private BigDecimal provisionalReceiptsAmount;

    //回收设备减值准备余额
    private BigDecimal equipmentDepreciationReservesBalance;

    //回收设备减值准备发生额
    private BigDecimal equipmentDepreciationReservesAmount;

    //回收设备减值损失余额
    private BigDecimal equipmentDepreciationLossBalance;

    //回收设备减值损失发生额
    private BigDecimal equipmentDepreciationLossAmount;

    //资产处置收益余额
    private BigDecimal assetDisposeGainBalance;

    //资产处置收益发生额
    private BigDecimal assetDisposeGainAmount;

    //资产处置损失余额
    private BigDecimal assetDisposeLossBalance;

    //资产处置损失发生额
    private BigDecimal assetDisposeLossAmount;

    //其他应收款余额
    private BigDecimal receivableOtherBalance;

    //其他应收款发生额
    private BigDecimal receivableOtherAmount;

    //应收转让后收款余额
    private BigDecimal receivableCollectionTransferBalance;

    //应收转让后收款发生额
    private BigDecimal receivableCollectionTransferAmount;

    //其他应收款_关联公司往来余额
    private BigDecimal receivableRelatedPartyBalance;

    //其他应收款_关联公司往来发生额
    private BigDecimal receivableRelatedPartyAmount;

    //代收转让款项余额
    private BigDecimal collectionTransferBalance;

    //代收转让款项发生额
    private BigDecimal collectionTransferAmount;

    //其他代收款余额
    private BigDecimal collectPaymentOtherBalance;

    //其他代收款发生额
    private BigDecimal collectPaymentOtherAmount;

    //应收利息余额
    private BigDecimal receivableInterestBalance;

    //应收利息发生额
    private BigDecimal receivableInterestAmount;

    //应收诉讼保证金余额
    private BigDecimal receivableLitigationMarginBalance;

    //应收诉讼保证金发生额
    private BigDecimal receivableLitigationMarginAmount;

    //房产_成本余额
    private BigDecimal propertyCostBalance;

    //房产_成本发生额
    private BigDecimal propertyCostAmount;

    //房产_减值准备 余额
    private BigDecimal propertyDepreciationReservesBalance;

    //房产_减值准备 发生额
    private BigDecimal propertyDepreciationReservesAmount;

    //机器设备_成本余额
    private BigDecimal machineCostBalance;

    //机器设备_成本发生额
    private BigDecimal machineCostAmount;

    //机器设备_减值准备余额
    private BigDecimal machineDepreciationReservesBalance;

    //机器设备_减值准备发生额
    private BigDecimal machineDepreciationReservesAmount;

    //其他_成本余额
    private BigDecimal otherCostBalance;

    //其他_成本发生额
    private BigDecimal otherCostAmount;

    //其他_减值准备余额
    private BigDecimal otherDepreciationReservesBalance;

    //其他_减值准备发生额
    private BigDecimal otherDepreciationReservesAmount;

    //应收保理本金余额
    private BigDecimal receivableFactoringPrincipalBalance;

    //应收保理本金发生额
    private BigDecimal receivableFactoringPrincipalAmount;

    //应收保理利息调整余额
    private BigDecimal receivableFactoringInterestBalance;

    //应收保理利息调整发生额
    private BigDecimal receivableFactoringInterestAmount;

    //应收贴息手续费_销项税余额
    private BigDecimal payableDiscountCostOuttaxBalance;

    //应收贴息手续费_销项税发生额
    private BigDecimal payableDiscountCostOuttaxAmount;

    //投资性房地产应收租金	余额
    private BigDecimal receivableRentInvestmentPropertyBalance;

    //投资性房地产应收租金	发生额
    private BigDecimal receivableRentInvestmentPropertyAmount;

    //投资性房地产应收销项税	余额
    private BigDecimal receivableOuttaxInvestmentPropertyBalance;

    //投资性房地产应收销项税	发生额
    private BigDecimal receivableOuttaxInvestmentPropertyAmount;

    //应收贸易款坏账准备余额
    private BigDecimal depreciationReservesTradeBalance;

    //应收贸易款坏账准备发生额
    private BigDecimal depreciationReservesTradeAmount;

    //应收服务费坏账准备余额
    private BigDecimal depreciationReservesServiceBalance;

    //应收服务费坏账准备发生额
    private BigDecimal depreciationReservesServiceAmount;

    //其他应收款项坏账准备余额
    private BigDecimal depreciationReservesOtherReceivableBalance;

    //其他应收款项坏账准备发生额
    private BigDecimal depreciationReservesOtherReceivableAmount;

    //暂支及个人往来坏账准备余额
    private BigDecimal depreciationReservesTempBalance;

    //暂支及个人往来坏账准备发生额
    private BigDecimal depreciationReservesTempAmount;

    //其他保证金坏账准备余额
    private BigDecimal depreciationReservesOtherMarginBalance;

    //其他保证金坏账准备发生额
    private BigDecimal depreciationReservesOtherMarginAmount;

    //投资性房地产应收租金坏账准备余额
    private BigDecimal depreciationReservesInvestmentPropertyBalance;

    //投资性房地产应收租金坏账准备发生额
    private BigDecimal depreciationReservesInvestmentPropertyAmount;

    //押金坏账准备余额
    private BigDecimal depreciationReservesDepositBalance;

    //押金坏账准备发生额
    private BigDecimal depreciationReservesDepositAmount;

    //应收诉讼保全费坏账准备余额
    private BigDecimal depreciationReservesLitigationBalance;

    //应收诉讼保全费坏账准备发生额
    private BigDecimal depreciationReservesLitigationAmount;

    //应收诉讼保证金坏账准备余额
    private BigDecimal depreciationReservesLitigationMarginBalance;

    //应收诉讼保证金坏账准备发生额
    private BigDecimal depreciationReservesLitigationMarginAmount;

    //其他坏账准备余额
    private BigDecimal depreciationReservesOtherBalance;

    //其他坏账准备发生额
    private BigDecimal depreciationReservesOtherAmount;

    //减值准备_单项余额
    private BigDecimal depreciationReservesIndividualBalance;

    //减值准备_单项发生额
    private BigDecimal depreciationReservesIndividualAmount;

    //应收票据坏账准备余额
    private BigDecimal depreciationReservesBillBalance;

    //应收票据坏账准备发生额
    private BigDecimal depreciationReservesBillAmount;

    //长期应收政府与社会资本合作项目减值准备余额
    private BigDecimal depreciationReservesGovBalance;

    //长期应收政府与社会资本合作项目减值准备发生额
    private BigDecimal depreciationReservesGovAmount;

    //其他长期应收款减值准备余额
    private BigDecimal depreciationReservesOtherLongReceiblesBalance;

    //其他长期应收款减值准备发生额
    private BigDecimal depreciationReservesOtherLongReceiblesAmount;

    //长期应收款关联公司往来减值准备余额
    private BigDecimal depreciationReservesLongRelatedBalance;

    //长期应收款关联公司往来减值准备发生额
    private BigDecimal depreciationReservesLongRelatedAmount;

    //借款应收利息减值准备余额
    private BigDecimal depreciationReservesInterestBalance;

    //借款应收利息减值准备发生额
    private BigDecimal depreciationReservesInterestAmount;

    //本金余额
    private BigDecimal receivablePrincipalBalance;

    //本金发生额
    private BigDecimal receivablePrincipalAmount;

    //利息调整余额
    private BigDecimal receivableInterestAdjustmentBalance;

    //利息调整发生额
    private BigDecimal receivableInterestAdjustmentAmount;

    //贷款_非金融机构_本金余额
    private BigDecimal receivablePrincipalNonfinancialBalance;

    //贷款_非金融机构_本金发生额
    private BigDecimal receivablePrincipalNonfinancialAmount;

    //贷款_非金融机构_应收手续费余额
    private BigDecimal receivableCommissionNonfinancialBalance;

    //贷款_非金融机构_应收手续费发生额
    private BigDecimal receivableCommissionNonfinancialAmount;

    //贷款_非金融机构_利息调整余额
    private BigDecimal receivableInterestAdjustmentNonfinancialBalance;

    //贷款_非金融机构_利息调整发生额
    private BigDecimal receivableInterestAdjustmentNonfinancialAmount;

    //银行存款减值准备余额
    private BigDecimal depreciationReservesBankBalance;

    //银行存款减值准备发生额
    private BigDecimal depreciationReservesBankAmount;

    //买入返售金融资产减值准备余额
    private BigDecimal depreciationReservesBuyingBackBalance;

    //买入返售金融资产减值准备发生额
    private BigDecimal depreciationReservesBuyingBackAmount;

    //定期存款应收利息减值准备余额
    private BigDecimal depreciationReservesTermDepositInterestBalance;

    //定期存款应收利息减值准备发生额
    private BigDecimal depreciationReservesTermDepositInterestAmount;

    //借款应收利息减值准备余额
    private BigDecimal depreciationReservesBorrowingsInterestBalance;

    //借款应收利息减值准备发生额
    private BigDecimal depreciationReservesBorrowingsInterestAmount;

    //买入返售金融资产应收利息减值准备余额
    private BigDecimal depreciationReservesBuyingBackInterestBalance;

    //买入返售金融资产应收利息减值准备发生额
    private BigDecimal depreciationReservesBuyingBackInterestAmount;

    //银行理财产品应收利息减值准备余额
    private BigDecimal depreciationReservesFinancialProductInterestBalance;

    //银行理财产品应收利息减值准备发生额
    private BigDecimal depreciationReservesFinancialProductInterestAmount;

    //结构性存款应收利息减值准备余额
    private BigDecimal depreciationReservesStructuredDepositInterestBalance;

    //结构性存款应收利息减值准备发生额
    private BigDecimal depreciationReservesStructuredDepositInterestAmount;

    //FVPL_应收利息减值准备余额
    private BigDecimal depreciationReservesFvplInterestOtherBalance;

    //FVPL_应收利息减值准备发生额
    private BigDecimal depreciationReservesFvplInterestOtherAmount;

    //FVOCI_应收利息减值准备余额
    private BigDecimal depreciationReservesBondFvociInterestBalance;

    //FVOCI_应收利息减值准备发生额
    private BigDecimal depreciationReservesBondFvociInterestAmount;

    //FVOCI_其他应收利息减值准备余额
    private BigDecimal depreciationReservesFvociInterestOtherBalance;

    //FVOCI_其他应收利息减值准备发生额
    private BigDecimal depreciationReservesFvociInterestOtherAmount;

    //以摊余成本计量的债券应收利息减值准备余额
    private BigDecimal depreciationReservesBondAcInterestBalance;

    //以摊余成本计量的债券应收利息减值准备发生额
    private BigDecimal depreciationReservesBondAcInterestAmount;

    //其他以摊余成本计量的金融资产应收利息减值准备余额
    private BigDecimal depreciationReservesAcInterestOtherBalance;

    //其他以摊余成本计量的金融资产应收利息减值准备发生额
    private BigDecimal depreciationReservesAcInterestOtherAmount;

    //以摊余成本计量的债券减值准备余额
    private BigDecimal depreciationReservesBondAcBalance;

    //以摊余成本计量的债券减值准备发生额
    private BigDecimal depreciationReservesBondAcAmount;

    //以摊余成本计量的信托计划减值准备余额
    private BigDecimal depreciationReservesTurstAcBalance;

    //以摊余成本计量的信托计划减值准备发生额
    private BigDecimal depreciationReservesTurstAcAmount;

    //以摊余成本计量的其他金融资产减值准备余额
    private BigDecimal depreciationReservesOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值准备发生额
    private BigDecimal depreciationReservesOtherFinancialAcAmount;

    //以摊余成本计量的其他资产减值准备余额
    private BigDecimal depreciationReservesOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值准备发生额
    private BigDecimal depreciationReservesOtherAssetAcAmount;

    //FVOCI_债券减值准备余额
    private BigDecimal depreciationReservesBondFvociBalance;

    //FVOCI_债券减值准备发生额
    private BigDecimal depreciationReservesBondFvociAmount;

    //FVOCI_其他金融资产减值准备余额
    private BigDecimal depreciationReservesOtherFinancialFvociBalance;

    //FVOCI_其他金融资产减值准备发生额
    private BigDecimal depreciationReservesOtherFinancialFvociAmount;

    //投资子公司减值准备余额
    private BigDecimal depreciationReservesSubsidiaryBalance;

    //投资子公司减值准备发生额
    private BigDecimal depreciationReservesSubsidiaryAmount;

    //投资合营企业减值准备余额
    private BigDecimal depreciationReservesJvBalance;

    //投资合营企业减值准备发生额
    private BigDecimal depreciationReservesJvAmount;

    //投资联营企业减值准备余额
    private BigDecimal depreciationReservesAssociateBalance;

    //投资联营企业减值准备发生额
    private BigDecimal depreciationReservesAssociateAmount;

    //虚拟收付款余额
    private BigDecimal receivableVirtualBalance;

    //虚拟收付款发生额
    private BigDecimal receivableVirtualAmount;

    //未实现其他收益余额
    private BigDecimal unrealizedRevenueOtherBalance;

    //未实现其他收益发生额
    private BigDecimal unrealizedRevenueOtherAmount;

    //应付保理款余额
    private BigDecimal payableFactoringBalance;

    //应付保理款发生额
    private BigDecimal payableFactoringAmount;

    //应付委贷款余额
    private BigDecimal payableEntrustBalance;

    //应付委贷款发生额
    private BigDecimal payableEntrustAmount;

    //代收理赔款余额
    private BigDecimal collectClaimsBalance;

    //代收理赔款发生额
    private BigDecimal collectClaimsAmount;

    //应付其他款项余额
    private BigDecimal payableOtherBalance;

    //应付其他款项发生额
    private BigDecimal payableOtherAmount;

    //预收租赁款余额
    private BigDecimal prereceivedRentBalance;

    //预收租赁款发生额
    private BigDecimal prereceivedRentAmount;

    //其他应付款_关联公司往来	余额
    private BigDecimal payableRelatedPartyBalance;

    //其他应付款_关联公司往来	发生额
    private BigDecimal payableRelatedPartyAmount;

    //其他应付款_资产支持专项计划余额
    private BigDecimal otherPayableSpvBalance;

    //其他应付款_资产支持专项计划发生额
    private BigDecimal otherPayableSpvAmount;

    //其他应付款_信托计划余额
    private BigDecimal otherPayableTrustBalance;

    //其他应付款_信托计划发生额
    private BigDecimal otherPayableTrustAmount;

    //其他应付款_出表保理资产余额
    private BigDecimal otherPayableClaimAssetBalance;

    //其他应付款_出表保理资产发生额
    private BigDecimal otherPayableClaimAssetAmount;

    //其他应付款_租金余额余额
    private BigDecimal otherPayableRentBalance;

    //其他应付款_租金余额发生额
    private BigDecimal otherPayableRentAmount;

    //其他应付款_残值余额余额
    private BigDecimal otherPayableResidualBalance;

    //其他应付款_残值余额发生额
    private BigDecimal otherPayableResidualAmount;

    //其他应付款_其他余额
    private BigDecimal otherPayableOtherBalance;

    //其他应付款_其他发生额
    private BigDecimal otherPayableOtherAmount;

    //其他应付款_保理款余额余额
    private BigDecimal otherPayableClaimBalance;

    //其他应付款_保理款余额发生额
    private BigDecimal otherPayableClaimAmount;

    //其他应付款_保理款余额_其他余额
    private BigDecimal otherPayableClaimOtherBalance;

    //其他应付款_保理款余额_其他发生额
    private BigDecimal otherPayableClaimOtherAmount;

    //其他应付款_代收出表保理资产款项余额
    private BigDecimal otherPayableCollectBalance;

    //其他应付款_代收出表保理资产款项发生额
    private BigDecimal otherPayableCollectAmount;

    //供应商资金池余额
    private BigDecimal supplierPoolBalance;

    //供应商资金池发生额
    private BigDecimal supplierPoolAmount;

    //代理商保证金余额
    private BigDecimal agentMarginBalance;

    //代理商保证金发生额
    private BigDecimal agentMarginAmount;

    //其他保证金余额
    private BigDecimal otherMarginBalance;

    //其他保证金发生额
    private BigDecimal otherMarginAmount;

    //房屋租赁保证金余额
    private BigDecimal rentMarginBalance;

    //房屋租赁保证金发生额
    private BigDecimal rentMarginAmount;

    //其他长期应付保证金余额
    private BigDecimal otherLongMarginBalance;

    //其他长期应付保证金发生额
    private BigDecimal otherLongMarginAmount;

    //其他主营业务收入	余额
    private BigDecimal otherBusinessIncomeBalance;

    //其他主营业务收入	发生额
    private BigDecimal otherBusinessIncomeAmount;

    //其他主营业务收入_手续费收入余额
    private BigDecimal otherIncomeServiceBalance;

    //其他主营业务收入_手续费收入发生额
    private BigDecimal otherIncomeServiceAmount;

    //其他长期应收款利息收入余额
    private BigDecimal interestOtherLongPayablesBalance;

    //其他长期应收款利息收入发生额
    private BigDecimal interestOtherLongPayablesAmount;

    //以摊余成本计量的其他金融资产利息收入余额
    private BigDecimal interestOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产利息收入发生额
    private BigDecimal interestOtherFinancialAcAmount;

    //租赁收益6%余额
    private BigDecimal leaseRevenue6Balance;

    //租赁收益6%发生额
    private BigDecimal leaseRevenue6Amount;

    //租赁收益3%余额
    private BigDecimal leaseRevenue3Balance;

    //租赁收益3%发生额
    private BigDecimal leaseRevenue3Amount;

    //其他业务收入_融资租赁款转让收益余额
    private BigDecimal otherIncomeLeaseTransferBalance;

    //其他业务收入_融资租赁款转让收益发生额
    private BigDecimal otherIncomeLeaseTransferAmount;

    //投资性房地产租金收入余额
    private BigDecimal rentInvestmentPropertyBalance;

    //投资性房地产租金收入发生额
    private BigDecimal rentInvestmentPropertyAmount;

    //其他业务收入_应收保理款转让收益余额
    private BigDecimal otherIncomeFactoringTransferBalance;

    //其他业务收入_应收保理款转让收益发生额
    private BigDecimal otherIncomeFactoringTransferAmount;

    //抵债资产处置收益余额
    private BigDecimal assetDisposeGainForeclosedBalance;

    //抵债资产处置收益发生额
    private BigDecimal assetDisposeGainForeclosedAmount;

    //抵债资产处置损失余额
    private BigDecimal assetDisposeLossForeclosedBalance;

    //抵债资产处置损失发生额
    private BigDecimal assetDisposeLossForeclosedAmount;

    //其他业务成本_融资租赁款转让收益余额
    private BigDecimal otherCostLeaseTransferBalance;

    //其他业务成本_融资租赁款转让收益发生额
    private BigDecimal otherCostLeaseTransferAmount;

    //其他业务成本_应收保理款转让收益余额
    private BigDecimal otherCostFactoringTransferBalance;

    //其他业务成本_应收保理款转让收益发生额
    private BigDecimal otherCostFactoringTransferAmount;

    //评估费余额
    private BigDecimal assessmentFeeBalance;

    //评估费发生额
    private BigDecimal assessmentFeeAmount;

    //律师费余额
    private BigDecimal attorneyFeeBalance;

    //律师费发生额
    private BigDecimal attorneyFeeAmount;

    //其他聘请中介机构费余额
    private BigDecimal intermediaryFeeOtherBalance;

    //其他聘请中介机构费发生额
    private BigDecimal intermediaryFeeOtherAmount;

    //咨询费余额
    private BigDecimal consultationFeeBalance;

    //咨询费发生额
    private BigDecimal consultationFeeAmount;

    //公证费余额
    private BigDecimal notaryFeeBalance;

    //公证费发生额
    private BigDecimal notaryFeeAmount;

    //回收租赁资产杂费余额
    private BigDecimal leaseAssetRecoveryFeeBalance;

    //回收租赁资产杂费发生额
    private BigDecimal leaseAssetRecoveryFeeAmount;

    //应收贸易款坏账损失余额
    private BigDecimal depreciationLossTradeBalance;

    //应收贸易款坏账损失发生额
    private BigDecimal depreciationLossTradeAmount;

    //应收服务费坏账损失余额
    private BigDecimal depreciationLossServiceBalance;

    //应收服务费坏账损失发生额
    private BigDecimal depreciationLossServiceAmount;

    //其他应收款项坏账损失余额
    private BigDecimal depreciationLossOtherReceivablesBalance;

    //其他应收款项坏账损失发生额
    private BigDecimal depreciationLossOtherReceivablesAmount;

    //暂支及个人往来坏账损失余额
    private BigDecimal depreciationLossTempBalance;

    //暂支及个人往来坏账损失发生额
    private BigDecimal depreciationLossTempAmount;

    //其他保证金坏账损失余额
    private BigDecimal depreciationLossOtherMarginBalance;

    //其他保证金坏账损失发生额
    private BigDecimal depreciationLossOtherMarginAmount;

    //投资性房地产应收租金减值损失余额
    private BigDecimal depreciationLossRentInvestmentPropertyBalance;

    //投资性房地产应收租金减值损失发生额
    private BigDecimal depreciationLossRentInvestmentPropertyAmount;

    //押金减值损失余额
    private BigDecimal depreciationLossDepositBalance;

    //押金减值损失发生额
    private BigDecimal depreciationLossDepositAmount;

    //应收诉讼保全费减值损失余额
    private BigDecimal depreciationLossLitigationBalance;

    //应收诉讼保全费减值损失发生额
    private BigDecimal depreciationLossLitigationAmount;

    //应收诉讼保证金减值损失余额
    private BigDecimal depreciationLossLitigationMarginBalance;

    //应收诉讼保证金减值损失发生额
    private BigDecimal depreciationLossLitigationMarginAmount;

    //减值损失_其他余额
    private BigDecimal depreciationLossOtherReceivableBalance;

    //减值损失_其他发生额
    private BigDecimal depreciationLossOtherReceivableAmount;

    //应收融资租赁款减值损失_坏账注销转回余额
    private BigDecimal depreciationLossReverseBalance;

    //应收融资租赁款减值损失_坏账注销转回发生额
    private BigDecimal depreciationLossReverseAmount;

    //长期股权投资减值损失余额
    private BigDecimal depreciationLossEquityInvestmentBalance;

    //长期股权投资减值损失发生额
    private BigDecimal depreciationLossEquityInvestmentAmount;

    //应收利息减值损失余额
    private BigDecimal depreciationLossInterestBalance;

    //应收利息减值损失发生额
    private BigDecimal depreciationLossInterestAmount;

    //应收票据减值损失余额
    private BigDecimal depreciationLossBillBalance;

    //应收票据减值损失发生额
    private BigDecimal depreciationLossBillAmount;

    //抵债资产减值损失余额
    private BigDecimal depreciationLossCollateralBalance;

    //抵债资产减值损失发生额
    private BigDecimal depreciationLossCollateralAmount;

    //金融资产减值损失余额
    private BigDecimal depreciationLossFinancialBalance;

    //金融资产减值损失发生额
    private BigDecimal depreciationLossFinancialAmount;

    //买入返售金融资产减值损失余额
    private BigDecimal depreciationLossBuyingBackBalance;

    //买入返售金融资产减值损失发生额
    private BigDecimal depreciationLossBuyingBackAmount;

    //以摊余成本计量的债券减值损失余额
    private BigDecimal depreciationLossBondAcBalance;

    //以摊余成本计量的债券减值损失发生额
    private BigDecimal depreciationLossBondAcAmount;

    //FVOCI_金融资产减值损失余额
    private BigDecimal depreciationLossFinancialFvociBalance;

    //FVOCI_金融资产减值损失发生额
    private BigDecimal depreciationLossFinancialFvociAmount;

    //银行存款减值损失余额
    private BigDecimal depreciationLossBankBalance;

    //银行存款减值损失发生额
    private BigDecimal depreciationLossBankAmount;

    //以摊余成本计量的信托计划减值损失余额
    private BigDecimal depreciationLossTrustAcBalance;

    //以摊余成本计量的信托计划减值损失发生额
    private BigDecimal depreciationLossTrustAcAmount;

    //以摊余成本计量的其他资产减值损失余额
    private BigDecimal depreciationLossOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值损失发生额
    private BigDecimal depreciationLossOtherAssetAcAmount;

    //以摊余成本计量的其他金融资产减值损失余额
    private BigDecimal depreciationLossOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值损失发生额
    private BigDecimal depreciationLossOtherFinancialAcAmount;

    //长期应收政府与社会资本合作项目减值损失余额
    private BigDecimal depreciationLossGovBalance;

    //长期应收政府与社会资本合作项目减值损失发生额
    private BigDecimal depreciationLossGovAmount;

    //其他长期应收款减值损失余额
    private BigDecimal depreciationLossOtherLongReceivablesBalance;

    //其他长期应收款减值损失发生额
    private BigDecimal depreciationLossOtherLongReceivablesAmount;

    //长期应收款关联公司往来减值损失余额
    private BigDecimal depreciationLossOtherLongRelatedBalance;

    //长期应收款关联公司往来减值损失发生额
    private BigDecimal depreciationLossOtherLongRelatedAmount;

    //借款应收利息减值损失余额
    private BigDecimal depreciationLossBorrowingsInterestBalance;

    //借款应收利息减值损失发生额
    private BigDecimal depreciationLossBorrowingsInterestAmount;

    //投资性房地产减值损失余额
    private BigDecimal depreciationLossInvestmentPropertyBalance;

    //投资性房地产减值损失发生额
    private BigDecimal depreciationLossInvestmentPropertyAmount;

    //其他减值损失余额
    private BigDecimal depreciationLossOtherBalance;

    //其他减值损失发生额
    private BigDecimal depreciationLossOtherAmount;

    //公告费余额
    private BigDecimal publicationFeeBalance;

    //公告费发生额
    private BigDecimal publicationFeeAmount;

    //债务重组项目应收总额	余额
    private BigDecimal receiveSumDebtRestructureBalance;

    //债务重组项目应收总额	发生额
    private BigDecimal receiveSumDebtRestructureAmount;

    //债务重组项目未实现收益余额
    private BigDecimal unrealizedRevenueDebtRestructureBalance;

    //债务重组项目未实现收益发生额
    private BigDecimal unrealizedRevenueDebtRestructureAmount;

    //债务重组项目应收销项税余额
    private BigDecimal receivableOuttaxDebtRestructureBalance;

    //债务重组项目应收销项税发生额
    private BigDecimal receivableOuttaxDebtRestructureAmount;

    //债务重组项目未实现其他收益余额
    private BigDecimal unrealizedRevenueOtherDebtRestructureBalance;

    //债务重组项目未实现其他收益发生额
    private BigDecimal unrealizedRevenueOtherDebtRestructureAmount;

    private Integer periodCode;

    private String billContractCode;
}
