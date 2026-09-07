package com.utfinancing.financehub.engine.finance.entity;

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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同余额表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_balance")
public class ContractBalanceEntity extends Model<ContractBalanceEntity> {

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
//    @TableLogic
    private String delFlag;

    //应收租金余额
    @TableField(exist = false)
    private BigDecimal receivableRentBalance;

    //应收租金发生额
    @TableField(exist = false)
    private BigDecimal receivableRentAmount;

    //应收首付款余额
    @TableField(exist = false)
    private BigDecimal receivableDownpaymentBalance;

    //应收首付款发生额
    @TableField(exist = false)
    private BigDecimal receivableDownpaymentAmount;

    //应收期末残值余额
    @TableField(exist = false)
    private BigDecimal receivableResidualValueBalance;

    //应收期末残值发生额
    @TableField(exist = false)
    private BigDecimal receivableResidualValueAmount;

    //应收手续费余额
    @TableField(exist = false)
    private BigDecimal receivableCommissionBalance;

    //应收手续费发生额
    @TableField(exist = false)
    private BigDecimal receivableCommissionAmount;

    //应收返利余额
    @TableField(exist = false)
    private BigDecimal receivableRebateBalance;

    //应收返利发生额
    @TableField(exist = false)
    private BigDecimal receivableRebateAmount;

    //应收保险费余额
    @TableField(exist = false)
    private BigDecimal receivableInsuranceBalance;

    //应收保险费发生额
    @TableField(exist = false)
    private BigDecimal receivableInsuranceAmount;

    //应收其他收入余额
    @TableField(exist = false)
    private BigDecimal receivableOtherincomeBalance;

    //应收其他收入发生额
    @TableField(exist = false)
    private BigDecimal receivableOtherincomeAmount;

    //应收销项税余额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxBalance;

    //应收销项税发生额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxAmount;

    //应收销项税-本金余额
    @TableField(exist = false)
    private BigDecimal receivableOutputtaxBaseBalance;

    //应收销项税-本金发生额
    @TableField(exist = false)
    private BigDecimal receivableOutputtaxBaseAmount;

    //未确认收款余额
    @TableField(exist = false)
    private BigDecimal receivableUnconfirmReceiptBalance;

    //未确认收款发生额
    @TableField(exist = false)
    private BigDecimal receivableUnconfirmReceiptAmount;

    //应收服务费余额
    @TableField(exist = false)
    private BigDecimal receivableServiceBalance;

    //应收服务费发生额
    @TableField(exist = false)
    private BigDecimal receivableServiceAmount;

    //应收服务费-销项税余额
    @TableField(exist = false)
    private BigDecimal receivableServiceOuttaxBalance;

    //应收服务费-销项税发生额
    @TableField(exist = false)
    private BigDecimal receivableServiceOuttaxAmount;

    //未实现收益余额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueBalance;

    //未实现收益发生额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueAmount;

    //融资租赁收益余额
    @TableField(exist = false)
    private BigDecimal leaseRevenueBalance;

    //融资租赁收益发生额
    @TableField(exist = false)
    private BigDecimal leaseRevenueAmount;

    //服务收入余额
    @TableField(exist = false)
    private BigDecimal serviceRevenueBalance;

    //服务收入发生额
    @TableField(exist = false)
    private BigDecimal serviceRevenueAmount;

    //保险费差额余额
    @TableField(exist = false)
    private BigDecimal insuranceDifferBalance;

    //保险费差额发生额
    @TableField(exist = false)
    private BigDecimal insuranceDifferAmount;

    //罚息收入余额
    @TableField(exist = false)
    private BigDecimal dinterestRevenueBalance;

    //罚息收入发生额
    @TableField(exist = false)
    private BigDecimal dinterestRevenueAmount;

    //合同解约及更改手续费余额
    @TableField(exist = false)
    private BigDecimal terminateBalance;

    //合同解约及更改手续费发生额
    @TableField(exist = false)
    private BigDecimal terminateAmount;

    //其他租赁相关收入余额
    @TableField(exist = false)
    private BigDecimal otherRevenueBalance;

    //其他租赁相关收入发生额
    @TableField(exist = false)
    private BigDecimal otherRevenueAmount;

    //违约金收入余额
    @TableField(exist = false)
    private BigDecimal damagesRevenueBalance;

    //违约金收入发生额
    @TableField(exist = false)
    private BigDecimal damagesRevenueAmount;

    //融资租赁业务保证金利息收入余额
    @TableField(exist = false)
    private BigDecimal marginInterestBalance;

    //融资租赁业务保证金利息收入发生额
    @TableField(exist = false)
    private BigDecimal marginInterestAmount;

    //应付租赁设备款-暂估余额
    @TableField(exist = false)
    private BigDecimal payableDeviceEstimateBalance;

    //应付租赁设备款-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableDeviceEstimateAmount;

    //应付租赁设备款余额
    @TableField(exist = false)
    private BigDecimal payableDeviceBalance;

    //应付租赁设备款发生额
    @TableField(exist = false)
    private BigDecimal payableDeviceAmount;

    //应付其他租赁成本-暂估余额
    @TableField(exist = false)
    private BigDecimal payableOtherCostEstimateBalance;

    //应付其他租赁成本-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableOtherCostEstimateAmount;

    //应付其他租赁成本余额
    @TableField(exist = false)
    private BigDecimal payableOtherCostBalance;

    //应付其他租赁成本发生额
    @TableField(exist = false)
    private BigDecimal payableOtherCostAmount;

    //应付经销商服务费-暂估余额
    @TableField(exist = false)
    private BigDecimal payableAgencyEstimateBalance;

    //应付经销商服务费-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableAgencyEstimateAmount;

    //应付经销商服务费余额
    @TableField(exist = false)
    private BigDecimal payableAgencyBalance;

    //应付经销商服务费发生额
    @TableField(exist = false)
    private BigDecimal payableAgencyAmount;

    //应付收车费-暂估余额
    @TableField(exist = false)
    private BigDecimal payableVehicleEstimateBalance;

    //应付收车费-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableVehicleEstimateAmount;

    //应付收车费余额
    @TableField(exist = false)
    private BigDecimal payableVehicleBalance;

    //应付收车费发生额
    @TableField(exist = false)
    private BigDecimal payableVehicleAmount;

    //应付手环成本_暂估余额
    @TableField(exist = false)
    private BigDecimal payableBandCostEstimateBalance;

    //应付手环成本_暂估发生额
    @TableField(exist = false)
    private BigDecimal payableBandCostEstimateAmount;

    //应付手环成本余额
    @TableField(exist = false)
    private BigDecimal payableBandCostBalance;

    //应付手环成本发生额
    @TableField(exist = false)
    private BigDecimal payableBandCostAmount;

    //应付抵押费_暂估余额
    @TableField(exist = false)
    private BigDecimal payablePledgeEstimateBalance;

    //应付抵押费_暂估发生额
    @TableField(exist = false)
    private BigDecimal payablePledgeEstimateAmount;

    //应付抵押费余额
    @TableField(exist = false)
    private BigDecimal payablePledgeBalance;

    //应付抵押费发生额
    @TableField(exist = false)
    private BigDecimal payablePledgeAmount;

    //应付解抵押费_暂估余额
    @TableField(exist = false)
    private BigDecimal payableUnpledgeEstimateBalance;

    //应付解抵押费_暂估发生额
    @TableField(exist = false)
    private BigDecimal payableUnpledgeEstimateAmount;

    //应付解抵押费余额
    @TableField(exist = false)
    private BigDecimal payableUnpledgeBalance;

    //应付解抵押费发生额
    @TableField(exist = false)
    private BigDecimal payableUnpledgeAmount;

    //承租人保证金余额
    @TableField(exist = false)
    private BigDecimal lesseeMarginBalance;

    //承租人保证金发生额
    @TableField(exist = false)
    private BigDecimal lesseeMarginAmount;

    //供应商及代理商保证金余额
    @TableField(exist = false)
    private BigDecimal supplierMarginBalance;

    //供应商及代理商保证金发生额
    @TableField(exist = false)
    private BigDecimal supplierMarginAmount;

    //应付保险费-暂估余额
    @TableField(exist = false)
    private BigDecimal payableInsuranceEstimateBalance;

    //应付保险费-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableInsuranceEstimateAmount;

    //应付保险费余额
    @TableField(exist = false)
    private BigDecimal payableInsuranceBalance;

    //应付保险费发生额
    @TableField(exist = false)
    private BigDecimal payableInsuranceAmount;

    //进项税额余额
    @TableField(exist = false)
    private BigDecimal intaxBalance;

    //进项税额发生额
    @TableField(exist = false)
    private BigDecimal intaxAmount;

    //销项税额余额
    @TableField(exist = false)
    private BigDecimal outtaxBalance;

    //销项税额发生额
    @TableField(exist = false)
    private BigDecimal outtaxAmount;

    //诉讼费余额
    @TableField(exist = false)
    private BigDecimal litigationExpensesBalance;

    //诉讼费发生额
    @TableField(exist = false)
    private BigDecimal litigationExpensesAmount;

    //减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBalance;

    //减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesAmount;

    //减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBalance;

    //减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossAmount;

    //回收融资租赁设备成本余额
    @TableField(exist = false)
    private BigDecimal receiveCostBalance;

    //回收融资租赁设备成本发生额
    @TableField(exist = false)
    private BigDecimal receiveCostAmount;

    //表外租赁收入余额
    @TableField(exist = false)
    private BigDecimal offIncomeBalance;

    //表外租赁收入发生额
    @TableField(exist = false)
    private BigDecimal offIncomeAmount;

    //递延收益余额
    @TableField(exist = false)
    private BigDecimal deferIncomeBalance;

    //递延收益发生额
    @TableField(exist = false)
    private BigDecimal deferIncomeAmount;

    //应付未付款余额
    @TableField(exist = false)
    private BigDecimal payableAccountBalance;

    //应付未付款发生额
    @TableField(exist = false)
    private BigDecimal payableAccountAmount;

    //应付保证金余额
    @TableField(exist = false)
    private BigDecimal payableMarginBalance;

    //应付保证金发生额
    @TableField(exist = false)
    private BigDecimal payableMarginAmount;

    //应付一年内保证金余额
    @TableField(exist = false)
    private BigDecimal payableMarginYearBalance;

    //应付一年内保证金发生额
    @TableField(exist = false)
    private BigDecimal payableMarginYearAmount;

    //保证金利息支出余额
    @TableField(exist = false)
    private BigDecimal marginOinterestBalance;

    //保证金利息支出发生额
    @TableField(exist = false)
    private BigDecimal marginOinterestAmount;

    //应收总额余额
    @TableField(exist = false)
    private BigDecimal receiveSumBalance;

    //应收总额发生额
    @TableField(exist = false)
    private BigDecimal receiveSumAmount;

    //应收总额_销项税余额
    @TableField(exist = false)
    private BigDecimal receiveSumOuttaxBalance;

    //应收总额_销项税发生额
    @TableField(exist = false)
    private BigDecimal receiveSumOuttaxAmount;

    //应收总额_未实现收益余额
    @TableField(exist = false)
    private BigDecimal receiveUnrealizedRevenueBalance;

    //应收总额_未实现收益发生额
    @TableField(exist = false)
    private BigDecimal receiveUnrealizedRevenueAmount;

    //应付一年内承租人保证金余额
    @TableField(exist = false)
    private BigDecimal payableLesseeMarginYearBalance;

    //应付一年内承租人保证金发生额
    @TableField(exist = false)
    private BigDecimal payableLesseeMarginYearAmount;

    //应付一年内供应商及代理商保证金余额
    @TableField(exist = false)
    private BigDecimal payableSupplierMarginYearBalance;

    //应付一年内供应商及代理商保证金发生额
    @TableField(exist = false)
    private BigDecimal payableSupplierMarginYearAmount;

    //应收诉讼费余额
    @TableField(exist = false)
    private BigDecimal receivableLitigationExpensesBalance;

    //应收诉讼费发生额
    @TableField(exist = false)
    private BigDecimal receivableLitigationExpensesAmount;

    //应付其他款项-暂估余额
    @TableField(exist = false)
    private BigDecimal payableOtherEstimateBalance;

    //应付其他款项-暂估发生额
    @TableField(exist = false)
    private BigDecimal payableOtherEstimateAmount;

    //应收贴息手续费余额
    @TableField(exist = false)
    private BigDecimal payableDiscountCostBalance;

    //应收贴息手续费发生额
    @TableField(exist = false)
    private BigDecimal payableDiscountCostAmount;

    //应付手续费成本余额
    @TableField(exist = false)
    private BigDecimal payableProcedureCostBalance;

    //应付手续费成本发生额
    @TableField(exist = false)
    private BigDecimal payableProcedureCostAmount;

    //暂收款项余额
    @TableField(exist = false)
    private BigDecimal provisionalReceiptsBalance;

    //暂收款项发生额
    @TableField(exist = false)
    private BigDecimal provisionalReceiptsAmount;

    //回收设备减值准备余额
    @TableField(exist = false)
    private BigDecimal equipmentDepreciationReservesBalance;

    //回收设备减值准备发生额
    @TableField(exist = false)
    private BigDecimal equipmentDepreciationReservesAmount;

    //回收设备减值损失余额
    @TableField(exist = false)
    private BigDecimal equipmentDepreciationLossBalance;

    //回收设备减值损失发生额
    @TableField(exist = false)
    private BigDecimal equipmentDepreciationLossAmount;

    //资产处置收益余额
    @TableField(exist = false)
    private BigDecimal assetDisposeGainBalance;

    //资产处置收益发生额
    @TableField(exist = false)
    private BigDecimal assetDisposeGainAmount;

    //资产处置损失余额
    @TableField(exist = false)
    private BigDecimal assetDisposeLossBalance;

    //资产处置损失发生额
    @TableField(exist = false)
    private BigDecimal assetDisposeLossAmount;

    //其他应收款余额
    @TableField(exist = false)
    private BigDecimal receivableOtherBalance;

    //其他应收款发生额
    @TableField(exist = false)
    private BigDecimal receivableOtherAmount;

    //应收转让后收款余额
    @TableField(exist = false)
    private BigDecimal receivableCollectionTransferBalance;

    //应收转让后收款发生额
    @TableField(exist = false)
    private BigDecimal receivableCollectionTransferAmount;

    //其他应收款_关联公司往来余额
    @TableField(exist = false)
    private BigDecimal receivableRelatedPartyBalance;

    //其他应收款_关联公司往来发生额
    @TableField(exist = false)
    private BigDecimal receivableRelatedPartyAmount;

    //代收转让款项余额
    @TableField(exist = false)
    private BigDecimal collectionTransferBalance;

    //代收转让款项发生额
    @TableField(exist = false)
    private BigDecimal collectionTransferAmount;

    //其他代收款余额
    @TableField(exist = false)
    private BigDecimal collectPaymentOtherBalance;

    //其他代收款发生额
    @TableField(exist = false)
    private BigDecimal collectPaymentOtherAmount;

    //应收利息余额
    @TableField(exist = false)
    private BigDecimal receivableInterestBalance;

    //应收利息发生额
    @TableField(exist = false)
    private BigDecimal receivableInterestAmount;

    //应收诉讼保证金余额
    @TableField(exist = false)
    private BigDecimal receivableLitigationMarginBalance;

    //应收诉讼保证金发生额
    @TableField(exist = false)
    private BigDecimal receivableLitigationMarginAmount;

    //房产_成本余额
    @TableField(exist = false)
    private BigDecimal propertyCostBalance;

    //房产_成本发生额
    @TableField(exist = false)
    private BigDecimal propertyCostAmount;

    //房产_减值准备 余额
    @TableField(exist = false)
    private BigDecimal propertyDepreciationReservesBalance;

    //房产_减值准备 发生额
    @TableField(exist = false)
    private BigDecimal propertyDepreciationReservesAmount;

    //机器设备_成本余额
    @TableField(exist = false)
    private BigDecimal machineCostBalance;

    //机器设备_成本发生额
    @TableField(exist = false)
    private BigDecimal machineCostAmount;

    //机器设备_减值准备余额
    @TableField(exist = false)
    private BigDecimal machineDepreciationReservesBalance;

    //机器设备_减值准备发生额
    @TableField(exist = false)
    private BigDecimal machineDepreciationReservesAmount;

    //其他_成本余额
    @TableField(exist = false)
    private BigDecimal otherCostBalance;

    //其他_成本发生额
    @TableField(exist = false)
    private BigDecimal otherCostAmount;

    //其他_减值准备余额
    @TableField(exist = false)
    private BigDecimal otherDepreciationReservesBalance;

    //其他_减值准备发生额
    @TableField(exist = false)
    private BigDecimal otherDepreciationReservesAmount;

    //应收保理本金余额
    @TableField(exist = false)
    private BigDecimal receivableFactoringPrincipalBalance;

    //应收保理本金发生额
    @TableField(exist = false)
    private BigDecimal receivableFactoringPrincipalAmount;

    //应收保理利息调整余额
    @TableField(exist = false)
    private BigDecimal receivableFactoringInterestBalance;

    //应收保理利息调整发生额
    @TableField(exist = false)
    private BigDecimal receivableFactoringInterestAmount;

    //应收贴息手续费_销项税余额
    @TableField(exist = false)
    private BigDecimal payableDiscountCostOuttaxBalance;

    //应收贴息手续费_销项税发生额
    @TableField(exist = false)
    private BigDecimal payableDiscountCostOuttaxAmount;

    //投资性房地产应收租金	余额
    @TableField(exist = false)
    private BigDecimal receivableRentInvestmentPropertyBalance;

    //投资性房地产应收租金	发生额
    @TableField(exist = false)
    private BigDecimal receivableRentInvestmentPropertyAmount;

    //投资性房地产应收销项税	余额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxInvestmentPropertyBalance;

    //投资性房地产应收销项税	发生额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxInvestmentPropertyAmount;

    //应收贸易款坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTradeBalance;

    //应收贸易款坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTradeAmount;

    //应收服务费坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesServiceBalance;

    //应收服务费坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesServiceAmount;

    //其他应收款项坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherReceivableBalance;

    //其他应收款项坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherReceivableAmount;

    //暂支及个人往来坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTempBalance;

    //暂支及个人往来坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTempAmount;

    //其他保证金坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherMarginBalance;

    //其他保证金坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherMarginAmount;

    //投资性房地产应收租金坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesInvestmentPropertyBalance;

    //投资性房地产应收租金坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesInvestmentPropertyAmount;

    //押金坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesDepositBalance;

    //押金坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesDepositAmount;

    //应收诉讼保全费坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLitigationBalance;

    //应收诉讼保全费坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLitigationAmount;

    //应收诉讼保证金坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLitigationMarginBalance;

    //应收诉讼保证金坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLitigationMarginAmount;

    //其他坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherBalance;

    //其他坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherAmount;

    //减值准备_单项余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesIndividualBalance;

    //减值准备_单项发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesIndividualAmount;

    //应收票据坏账准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBillBalance;

    //应收票据坏账准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBillAmount;

    //长期应收政府与社会资本合作项目减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesGovBalance;

    //长期应收政府与社会资本合作项目减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesGovAmount;

    //其他长期应收款减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherLongReceiblesBalance;

    //其他长期应收款减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherLongReceiblesAmount;

    //长期应收款关联公司往来减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLongRelatedBalance;

    //长期应收款关联公司往来减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesLongRelatedAmount;

    //借款应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesInterestBalance;

    //借款应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesInterestAmount;

    //本金余额
    @TableField(exist = false)
    private BigDecimal receivablePrincipalBalance;

    //本金发生额
    @TableField(exist = false)
    private BigDecimal receivablePrincipalAmount;

    //利息调整余额
    @TableField(exist = false)
    private BigDecimal receivableInterestAdjustmentBalance;

    //利息调整发生额
    @TableField(exist = false)
    private BigDecimal receivableInterestAdjustmentAmount;

    //贷款_非金融机构_本金余额
    @TableField(exist = false)
    private BigDecimal receivablePrincipalNonfinancialBalance;

    //贷款_非金融机构_本金发生额
    @TableField(exist = false)
    private BigDecimal receivablePrincipalNonfinancialAmount;

    //贷款_非金融机构_应收手续费余额
    @TableField(exist = false)
    private BigDecimal receivableCommissionNonfinancialBalance;

    //贷款_非金融机构_应收手续费发生额
    @TableField(exist = false)
    private BigDecimal receivableCommissionNonfinancialAmount;

    //贷款_非金融机构_利息调整余额
    @TableField(exist = false)
    private BigDecimal receivableInterestAdjustmentNonfinancialBalance;

    //贷款_非金融机构_利息调整发生额
    @TableField(exist = false)
    private BigDecimal receivableInterestAdjustmentNonfinancialAmount;

    //银行存款减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBankBalance;

    //银行存款减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBankAmount;

    //买入返售金融资产减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBuyingBackBalance;

    //买入返售金融资产减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBuyingBackAmount;

    //定期存款应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTermDepositInterestBalance;

    //定期存款应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTermDepositInterestAmount;

    //借款应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBorrowingsInterestBalance;

    //借款应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBorrowingsInterestAmount;

    //买入返售金融资产应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBuyingBackInterestBalance;

    //买入返售金融资产应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBuyingBackInterestAmount;

    //银行理财产品应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFinancialProductInterestBalance;

    //银行理财产品应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFinancialProductInterestAmount;

    //结构性存款应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesStructuredDepositInterestBalance;

    //结构性存款应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesStructuredDepositInterestAmount;

    //FVPL_应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFvplInterestOtherBalance;

    //FVPL_应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFvplInterestOtherAmount;

    //FVOCI_应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondFvociInterestBalance;

    //FVOCI_应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondFvociInterestAmount;

    //FVOCI_其他应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFvociInterestOtherBalance;

    //FVOCI_其他应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesFvociInterestOtherAmount;

    //以摊余成本计量的债券应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondAcInterestBalance;

    //以摊余成本计量的债券应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondAcInterestAmount;

    //其他以摊余成本计量的金融资产应收利息减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesAcInterestOtherBalance;

    //其他以摊余成本计量的金融资产应收利息减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesAcInterestOtherAmount;

    //以摊余成本计量的债券减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondAcBalance;

    //以摊余成本计量的债券减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondAcAmount;

    //以摊余成本计量的信托计划减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTurstAcBalance;

    //以摊余成本计量的信托计划减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesTurstAcAmount;

    //以摊余成本计量的其他金融资产减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherFinancialAcAmount;

    //以摊余成本计量的其他资产减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherAssetAcAmount;

    //FVOCI_债券减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondFvociBalance;

    //FVOCI_债券减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesBondFvociAmount;

    //FVOCI_其他金融资产减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherFinancialFvociBalance;

    //FVOCI_其他金融资产减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesOtherFinancialFvociAmount;

    //投资子公司减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesSubsidiaryBalance;

    //投资子公司减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesSubsidiaryAmount;

    //投资合营企业减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesJvBalance;

    //投资合营企业减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesJvAmount;

    //投资联营企业减值准备余额
    @TableField(exist = false)
    private BigDecimal depreciationReservesAssociateBalance;

    //投资联营企业减值准备发生额
    @TableField(exist = false)
    private BigDecimal depreciationReservesAssociateAmount;

    //虚拟收付款余额
    @TableField(exist = false)
    private BigDecimal receivableVirtualBalance;

    //虚拟收付款发生额
    @TableField(exist = false)
    private BigDecimal receivableVirtualAmount;

    //未实现其他收益余额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueOtherBalance;

    //未实现其他收益发生额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueOtherAmount;

    //应付保理款余额
    @TableField(exist = false)
    private BigDecimal payableFactoringBalance;

    //应付保理款发生额
    @TableField(exist = false)
    private BigDecimal payableFactoringAmount;

    //应付委贷款余额
    @TableField(exist = false)
    private BigDecimal payableEntrustBalance;

    //应付委贷款发生额
    @TableField(exist = false)
    private BigDecimal payableEntrustAmount;

    //代收理赔款余额
    @TableField(exist = false)
    private BigDecimal collectClaimsBalance;

    //代收理赔款发生额
    @TableField(exist = false)
    private BigDecimal collectClaimsAmount;

    //应付其他款项余额
    @TableField(exist = false)
    private BigDecimal payableOtherBalance;

    //应付其他款项发生额
    @TableField(exist = false)
    private BigDecimal payableOtherAmount;

    //预收租赁款余额
    @TableField(exist = false)
    private BigDecimal prereceivedRentBalance;

    //预收租赁款发生额
    @TableField(exist = false)
    private BigDecimal prereceivedRentAmount;

    //其他应付款_关联公司往来	余额
    @TableField(exist = false)
    private BigDecimal payableRelatedPartyBalance;

    //其他应付款_关联公司往来	发生额
    @TableField(exist = false)
    private BigDecimal payableRelatedPartyAmount;

    //其他应付款_资产支持专项计划余额
    @TableField(exist = false)
    private BigDecimal otherPayableSpvBalance;

    //其他应付款_资产支持专项计划发生额
    @TableField(exist = false)
    private BigDecimal otherPayableSpvAmount;

    //其他应付款_信托计划余额
    @TableField(exist = false)
    private BigDecimal otherPayableTrustBalance;

    //其他应付款_信托计划发生额
    @TableField(exist = false)
    private BigDecimal otherPayableTrustAmount;

    //其他应付款_出表保理资产余额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimAssetBalance;

    //其他应付款_出表保理资产发生额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimAssetAmount;

    //其他应付款_租金余额余额
    @TableField(exist = false)
    private BigDecimal otherPayableRentBalance;

    //其他应付款_租金余额发生额
    @TableField(exist = false)
    private BigDecimal otherPayableRentAmount;

    //其他应付款_残值余额余额
    @TableField(exist = false)
    private BigDecimal otherPayableResidualBalance;

    //其他应付款_残值余额发生额
    @TableField(exist = false)
    private BigDecimal otherPayableResidualAmount;

    //其他应付款_其他余额
    @TableField(exist = false)
    private BigDecimal otherPayableOtherBalance;

    //其他应付款_其他发生额
    @TableField(exist = false)
    private BigDecimal otherPayableOtherAmount;

    //其他应付款_保理款余额余额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimBalance;

    //其他应付款_保理款余额发生额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimAmount;

    //其他应付款_保理款余额_其他余额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimOtherBalance;

    //其他应付款_保理款余额_其他发生额
    @TableField(exist = false)
    private BigDecimal otherPayableClaimOtherAmount;

    //其他应付款_代收出表保理资产款项余额
    @TableField(exist = false)
    private BigDecimal otherPayableCollectBalance;

    //其他应付款_代收出表保理资产款项发生额
    @TableField(exist = false)
    private BigDecimal otherPayableCollectAmount;

    //供应商资金池余额
    @TableField(exist = false)
    private BigDecimal supplierPoolBalance;

    //供应商资金池发生额
    @TableField(exist = false)
    private BigDecimal supplierPoolAmount;

    //代理商保证金余额
    @TableField(exist = false)
    private BigDecimal agentMarginBalance;

    //代理商保证金发生额
    @TableField(exist = false)
    private BigDecimal agentMarginAmount;

    //其他保证金余额
    @TableField(exist = false)
    private BigDecimal otherMarginBalance;

    //其他保证金发生额
    @TableField(exist = false)
    private BigDecimal otherMarginAmount;

    //房屋租赁保证金余额
    @TableField(exist = false)
    private BigDecimal rentMarginBalance;

    //房屋租赁保证金发生额
    @TableField(exist = false)
    private BigDecimal rentMarginAmount;

    //其他长期应付保证金余额
    @TableField(exist = false)
    private BigDecimal otherLongMarginBalance;

    //其他长期应付保证金发生额
    @TableField(exist = false)
    private BigDecimal otherLongMarginAmount;

    //其他主营业务收入	余额
    @TableField(exist = false)
    private BigDecimal otherBusinessIncomeBalance;

    //其他主营业务收入	发生额
    @TableField(exist = false)
    private BigDecimal otherBusinessIncomeAmount;

    //其他主营业务收入_手续费收入余额
    @TableField(exist = false)
    private BigDecimal otherIncomeServiceBalance;

    //其他主营业务收入_手续费收入发生额
    @TableField(exist = false)
    private BigDecimal otherIncomeServiceAmount;

    //其他长期应收款利息收入余额
    @TableField(exist = false)
    private BigDecimal interestOtherLongPayablesBalance;

    //其他长期应收款利息收入发生额
    @TableField(exist = false)
    private BigDecimal interestOtherLongPayablesAmount;

    //以摊余成本计量的其他金融资产利息收入余额
    @TableField(exist = false)
    private BigDecimal interestOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产利息收入发生额
    @TableField(exist = false)
    private BigDecimal interestOtherFinancialAcAmount;

    //租赁收益6%余额
    @TableField(exist = false)
    private BigDecimal leaseRevenue6Balance;

    //租赁收益6%发生额
    @TableField(exist = false)
    private BigDecimal leaseRevenue6Amount;

    //租赁收益3%余额
    @TableField(exist = false)
    private BigDecimal leaseRevenue3Balance;

    //租赁收益3%发生额
    @TableField(exist = false)
    private BigDecimal leaseRevenue3Amount;

    //其他业务收入_融资租赁款转让收益余额
    @TableField(exist = false)
    private BigDecimal otherIncomeLeaseTransferBalance;

    //其他业务收入_融资租赁款转让收益发生额
    @TableField(exist = false)
    private BigDecimal otherIncomeLeaseTransferAmount;

    //投资性房地产租金收入余额
    @TableField(exist = false)
    private BigDecimal rentInvestmentPropertyBalance;

    //投资性房地产租金收入发生额
    @TableField(exist = false)
    private BigDecimal rentInvestmentPropertyAmount;

    //其他业务收入_应收保理款转让收益余额
    @TableField(exist = false)
    private BigDecimal otherIncomeFactoringTransferBalance;

    //其他业务收入_应收保理款转让收益发生额
    @TableField(exist = false)
    private BigDecimal otherIncomeFactoringTransferAmount;

    //抵债资产处置收益余额
    @TableField(exist = false)
    private BigDecimal assetDisposeGainForeclosedBalance;

    //抵债资产处置收益发生额
    @TableField(exist = false)
    private BigDecimal assetDisposeGainForeclosedAmount;

    //抵债资产处置损失余额
    @TableField(exist = false)
    private BigDecimal assetDisposeLossForeclosedBalance;

    //抵债资产处置损失发生额
    @TableField(exist = false)
    private BigDecimal assetDisposeLossForeclosedAmount;

    //其他业务成本_融资租赁款转让收益余额
    @TableField(exist = false)
    private BigDecimal otherCostLeaseTransferBalance;

    //其他业务成本_融资租赁款转让收益发生额
    @TableField(exist = false)
    private BigDecimal otherCostLeaseTransferAmount;

    //其他业务成本_应收保理款转让收益余额
    @TableField(exist = false)
    private BigDecimal otherCostFactoringTransferBalance;

    //其他业务成本_应收保理款转让收益发生额
    @TableField(exist = false)
    private BigDecimal otherCostFactoringTransferAmount;

    //评估费余额
    @TableField(exist = false)
    private BigDecimal assessmentFeeBalance;

    //评估费发生额
    @TableField(exist = false)
    private BigDecimal assessmentFeeAmount;

    //律师费余额
    @TableField(exist = false)
    private BigDecimal attorneyFeeBalance;

    //律师费发生额
    @TableField(exist = false)
    private BigDecimal attorneyFeeAmount;

    //其他聘请中介机构费余额
    @TableField(exist = false)
    private BigDecimal intermediaryFeeOtherBalance;

    //其他聘请中介机构费发生额
    @TableField(exist = false)
    private BigDecimal intermediaryFeeOtherAmount;

    //咨询费余额
    @TableField(exist = false)
    private BigDecimal consultationFeeBalance;

    //咨询费发生额
    @TableField(exist = false)
    private BigDecimal consultationFeeAmount;

    //公证费余额
    @TableField(exist = false)
    private BigDecimal notaryFeeBalance;

    //公证费发生额
    @TableField(exist = false)
    private BigDecimal notaryFeeAmount;

    //回收租赁资产杂费余额
    @TableField(exist = false)
    private BigDecimal leaseAssetRecoveryFeeBalance;

    //回收租赁资产杂费发生额
    @TableField(exist = false)
    private BigDecimal leaseAssetRecoveryFeeAmount;

    //应收贸易款坏账损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossTradeBalance;

    //应收贸易款坏账损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossTradeAmount;

    //应收服务费坏账损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossServiceBalance;

    //应收服务费坏账损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossServiceAmount;

    //其他应收款项坏账损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherReceivablesBalance;

    //其他应收款项坏账损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherReceivablesAmount;

    //暂支及个人往来坏账损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossTempBalance;

    //暂支及个人往来坏账损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossTempAmount;

    //其他保证金坏账损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherMarginBalance;

    //其他保证金坏账损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherMarginAmount;

    //投资性房地产应收租金减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossRentInvestmentPropertyBalance;

    //投资性房地产应收租金减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossRentInvestmentPropertyAmount;

    //押金减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossDepositBalance;

    //押金减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossDepositAmount;

    //应收诉讼保全费减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossLitigationBalance;

    //应收诉讼保全费减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossLitigationAmount;

    //应收诉讼保证金减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossLitigationMarginBalance;

    //应收诉讼保证金减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossLitigationMarginAmount;

    //减值损失_其他余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherReceivableBalance;

    //减值损失_其他发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherReceivableAmount;

    //应收融资租赁款减值损失_坏账注销转回余额
    @TableField(exist = false)
    private BigDecimal depreciationLossReverseBalance;

    //应收融资租赁款减值损失_坏账注销转回发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossReverseAmount;

    //长期股权投资减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossEquityInvestmentBalance;

    //长期股权投资减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossEquityInvestmentAmount;

    //应收利息减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossInterestBalance;

    //应收利息减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossInterestAmount;

    //应收票据减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBillBalance;

    //应收票据减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossBillAmount;

    //抵债资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossCollateralBalance;

    //抵债资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossCollateralAmount;

    //金融资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossFinancialBalance;

    //金融资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossFinancialAmount;

    //买入返售金融资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBuyingBackBalance;

    //买入返售金融资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossBuyingBackAmount;

    //以摊余成本计量的债券减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBondAcBalance;

    //以摊余成本计量的债券减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossBondAcAmount;

    //FVOCI_金融资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossFinancialFvociBalance;

    //FVOCI_金融资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossFinancialFvociAmount;

    //银行存款减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBankBalance;

    //银行存款减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossBankAmount;

    //以摊余成本计量的信托计划减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossTrustAcBalance;

    //以摊余成本计量的信托计划减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossTrustAcAmount;

    //以摊余成本计量的其他资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherAssetAcBalance;

    //以摊余成本计量的其他资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherAssetAcAmount;

    //以摊余成本计量的其他金融资产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherFinancialAcBalance;

    //以摊余成本计量的其他金融资产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherFinancialAcAmount;

    //长期应收政府与社会资本合作项目减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossGovBalance;

    //长期应收政府与社会资本合作项目减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossGovAmount;

    //其他长期应收款减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherLongReceivablesBalance;

    //其他长期应收款减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherLongReceivablesAmount;

    //长期应收款关联公司往来减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherLongRelatedBalance;

    //长期应收款关联公司往来减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherLongRelatedAmount;

    //借款应收利息减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossBorrowingsInterestBalance;

    //借款应收利息减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossBorrowingsInterestAmount;

    //投资性房地产减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossInvestmentPropertyBalance;

    //投资性房地产减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossInvestmentPropertyAmount;

    //其他减值损失余额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherBalance;

    //其他减值损失发生额
    @TableField(exist = false)
    private BigDecimal depreciationLossOtherAmount;

    //公告费余额
    @TableField(exist = false)
    private BigDecimal publicationFeeBalance;

    //公告费发生额
    @TableField(exist = false)
    private BigDecimal publicationFeeAmount;

    //债务重组项目应收总额	余额
    @TableField(exist = false)
    private BigDecimal receiveSumDebtRestructureBalance;

    //债务重组项目应收总额	发生额
    @TableField(exist = false)
    private BigDecimal receiveSumDebtRestructureAmount;

    //债务重组项目未实现收益余额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueDebtRestructureBalance;

    //债务重组项目未实现收益发生额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueDebtRestructureAmount;

    //债务重组项目应收销项税余额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxDebtRestructureBalance;

    //债务重组项目应收销项税发生额
    @TableField(exist = false)
    private BigDecimal receivableOuttaxDebtRestructureAmount;

    //债务重组项目未实现其他收益余额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueOtherDebtRestructureBalance;

    //债务重组项目未实现其他收益发生额
    @TableField(exist = false)
    private BigDecimal unrealizedRevenueOtherDebtRestructureAmount;

    //代收款项余额
    @TableField(exist = false)
    private BigDecimal collectPaymentBalance;

    //代收款项发生额
    @TableField(exist = false)
    private BigDecimal collectPaymentAmount;

    //应收罚息余额
    @TableField(exist = false)
    private BigDecimal receivableDefaultInterestBalance;

    //应收罚息发生额
    @TableField(exist = false)
    private BigDecimal receivableDefaultInterestAmount;

    //应收变更手续费余额
    @TableField(exist = false)
    private BigDecimal receivableTerminateBalance;

    //应收变更手续费发生额
    @TableField(exist = false)
    private BigDecimal receivableTerminateAmount;

    //诉讼费支付_虚拟余额
    @TableField(exist = false)
    private BigDecimal litigationVirtualBalance;

    //诉讼费支付_虚拟发生额
    @TableField(exist = false)
    private BigDecimal litigationVirtualAmount;

    //会计期间
    private Integer periodCode;

    //金蝶凭证ID
    private String easVoucherId;

    //借款合同编号
    private String billContractCode;

    //应收其他租赁相关收入余额
    @TableField(exist = false)
    private BigDecimal receivableOtherRevenueBalance;

    //应收其他租赁相关收入发生额
    @TableField(exist = false)
    private BigDecimal receivableOtherRevenueAmount;

    //应收违约金收入余额
    @TableField(exist = false)
    private BigDecimal receivableDamagesRevenueBalance;

    //应收违约金收入发生额
    @TableField(exist = false)
    private BigDecimal receivableDamagesRevenueAmount;

    //继续涉入资产余额
    @TableField(exist = false)
    private BigDecimal continueInvolvingAssetsBalance;

    //继续涉入资产发生额
    @TableField(exist = false)
    private BigDecimal continueInvolvingAssetsAmount;

    //应付委托贷款保证金余额
    @TableField(exist = false)
    private BigDecimal payableMarginEntrustedLoansBalance;

    //应付委托贷款保证金发生额
    @TableField(exist = false)
    private BigDecimal payableMarginEntrustedLoansAmount;

    //继续涉入负债余额
    @TableField(exist = false)
    private BigDecimal continueInvolvingDebtsBalance;

    //继续涉入负债发生额
    @TableField(exist = false)
    private BigDecimal continueInvolvingDebtsAmount;

    //债务重组投资收益余额
    @TableField(exist = false)
    private BigDecimal debtRestructuringIncomeBalance;

    //债务重组投资收益发生额
    @TableField(exist = false)
    private BigDecimal debtRestructuringIncomeAmount;

    //融资租赁款转让收益余额
    @TableField(exist = false)
    private BigDecimal leaseTransferIncomeBalance;

    //融资租赁款转让收益发生额
    @TableField(exist = false)
    private BigDecimal leaseTransferIncomeAmount;

    //其他业务成本余额
    @TableField(exist = false)
    private BigDecimal otherBusinessCostBalance;

    //其他业务成本发生额
    @TableField(exist = false)
    private BigDecimal otherBusinessCostAmount;

    //金融机构手续费余额
    @TableField(exist = false)
    private BigDecimal financialInstitutionFeeBalance;

    //金融机构手续费发生额
    @TableField(exist = false)
    private BigDecimal financialInstitutionFeeAmount;

    //应付票据余额
    @TableField(exist = false)
    private BigDecimal payableBillBalance;

    //应付票据发生额
    @TableField(exist = false)
    private BigDecimal payableBillAmount;

    //应收票据余额
    @TableField(exist = false)
    private BigDecimal receivableBillBalance;

    //应收票据发生额
    @TableField(exist = false)
    private BigDecimal receivableBillAmount;

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

    // 融资租赁资产-动产项目-回租发生额
    private BigDecimal leaseAssetMovableLeasebackAmount;

    // 融资租赁资产-动产项目-回租余额
    private BigDecimal leaseAssetMovableLeasebackBalance;

    // 融资租赁资产-在建动产项目-回租发生额
    private BigDecimal leaseAssetConstructionLeasebackAmount;

    // 融资租赁资产-在建动产项目-回租余额
    private BigDecimal leaseAssetConstructionLeasebackBalance;

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
