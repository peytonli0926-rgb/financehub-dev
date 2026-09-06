package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :   ContractBalance查询from对象
 * @Modified :
 */
@ApiModel("ContractBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

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

    @ApiModelProperty(value = "应收票据余额")
    private String receivableBillBalance;

    @ApiModelProperty(value = "应收票据发生额")
    private String receivableBillAmount;

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

    @ApiModelProperty(value = "中途手续费余额")
    private String midCommissionBalance;

    @ApiModelProperty(value = "中途手续费发生额")
    private String midCommissionAmount;

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

    @ApiModelProperty(value = "应付票据余额")
    private String payableBillBalance;

    @ApiModelProperty(value = "应付票据发生额")
    private String payableBillAmount;

    @ApiModelProperty(value = "银行存款余额")
    private String bankDepositBalance;

    @ApiModelProperty(value = "银行存款发生额")
    private String bankDepositAmount;

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

    @ApiModelProperty(value = "代收款项_租金余额")
    private String deputyRentBalance;

    @ApiModelProperty(value = "代收款项_租金发生额")
    private String deputyRentAmount;

    @ApiModelProperty(value = "代收款项_残值余额")
    private String deputyResidualValueBalance;

    @ApiModelProperty(value = "代收款项_残值发生额")
    private String deputyResidualValueAmount;

    @ApiModelProperty(value = "代收款项_其他余额")
    private String deputyOtherBalance;

    @ApiModelProperty(value = "代收款项_其他发生额")
    private String deputyOtherAmount;

    @ApiModelProperty(value = "回收融资租赁设备成本余额")
    private String receiveCostBalance;

    @ApiModelProperty(value = "回收融资租赁设备成本发生额")
    private String receiveCostAmount;

    @ApiModelProperty(value = "应收罚息余额")
    private String receiveDinterestBalance;

    @ApiModelProperty(value = "应收罚息发生额")
    private String receiveDinterestAmount;

    @ApiModelProperty(value = "应收变更手续费余额")
    private String receiveTerminateBalance;

    @ApiModelProperty(value = "应收变更手续费发生额")
    private String receiveTerminateAmount;

    @ApiModelProperty(value = "待转销项税余额")
    private String deputyOuttaxBalance;

    @ApiModelProperty(value = "待转销项税发生额")
    private String deputyOuttaxAmount;

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

    @ApiModelProperty(value = "法律费余额")
    private String lawExpensesBalance;

    @ApiModelProperty(value = "法律费发生额")
    private String lawExpensesAmount;

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

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "接口表ID")
    private Long interfaceDataId;

    @ApiModelProperty(value = "应收总额_利息收入余额")
    private String receiveInterestIncomeBalance;

    @ApiModelProperty(value = "应收总额_利息收入发生额")
    private String receiveInterestIncomeAmount;

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
}
