package com.utfinancing.financehub.engine.payment.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackAdditionalEventDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 四类补充业务事件共用接入流程，但按独立场景映射和校验。 */
@Service
@RequiredArgsConstructor
public class RetailLeasebackAdditionalEventService {
    public static final String REFUND = "CYC_REFUND";
    public static final String SUBSIDY = "CYC_SUBSIDY_CONFIRM";
    public static final String OVERDUE = "CYC_OVERDUE";
    public static final String STRUCTURE = "JYJGBG";
    public static final String AUXILIARY = "CYC_AUXILIARY_ADJUSTMENT";
    public static final String OTHER = "CYC_OTHER";
    private static final String BUSINESS_CODE = RetailLeasebackPaymentService.BUSINESS_CODE;

    private final IRuleService ruleService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final IFieldMappingService fieldMappingService;
    private final IContractService contractService;
    private final IBankAccountService bankAccountService;

    public List<VoucherDTO> executeRefund(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, REFUND, "refundEventCode", "退款"); }
    public List<VoucherDTO> executeSubsidy(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, SUBSIDY, "subsidyEventCode", "贴息确认"); }
    public List<VoucherDTO> executeOverdue(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, OVERDUE, "overdueEventCode", "逾期"); }
    public List<VoucherDTO> executeStructure(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, STRUCTURE, "structureEventCode", "交易结构变更"); }
    public List<VoucherDTO> executeAuxiliary(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, AUXILIARY, "auxiliaryEventCode", "辅助账调整"); }
    public List<VoucherDTO> executeOther(RetailLeasebackAdditionalEventDTO dto) { return execute(dto, OTHER, "otherEventCode", "其他"); }

    private List<VoucherDTO> execute(RetailLeasebackAdditionalEventDTO dto, String expectedScene, String eventField, String sceneName) {
        normalize(dto);
        if (rawTransactionDataService.lambdaQuery().eq(RawTransactionDataEntity::getOrderId, dto.getOrderId())
                .eq(RawTransactionDataEntity::getDelFlag, "0").exists()) {
            throw new ServiceException("请求流水号已存在: " + dto.getOrderId());
        }
        ContractEntity contract = resolveContract(dto.getContractCode());
        String event = resolveEvent(dto.getEventCode(), expectedScene, eventField, sceneName);
        validate(dto, expectedScene, event);
        if (REFUND.equals(expectedScene)) validateBankAccount(dto.getBankAccountNo(), contract);
        if (OTHER.equals(expectedScene) && ("OT001".equals(event) || "OT002".equals(event))) {
            validateBankAccount(dto.getSourceBankAccountNo(), contract);
            validateBankAccount(dto.getTargetBankAccountNo(), contract);
            if (StrUtil.equals(dto.getSourceBankAccountNo(), dto.getTargetBankAccountNo()))
                throw new ServiceException("转出银行账号和转入银行账号不能相同");
        } else if (OTHER.equals(expectedScene) && ("OT003".equals(event) || "OT004".equals(event))) {
            validateBankAccount(dto.getTargetBankAccountNo(), contract);
        }

        Map<String, Object> data = toRuleData(dto, contract, expectedScene, sceneName, eventField, event);
        RawTransactionDataEntity raw = rawTransactionDataService.saveRawData(data);
        rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.RUNNING.getCode(), null);
        data.put("interfaceId", raw.getId());
        data.put("interfaceCreateTime", raw.getCreateTime());
        try {
            List<VoucherDTO> vouchers = ruleService.executeRule(data);
            rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.SUCCESS.getCode(), null);
            return vouchers;
        } catch (RuntimeException ex) {
            rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.FAILED.getCode(), ex.getMessage());
            throw ex;
        }
    }

    private ContractEntity resolveContract(String contractCode) {
        ContractEntity c = contractService.lambdaQuery().eq(ContractEntity::getContractCode, contractCode)
                .eq(ContractEntity::getBusinessCode, BUSINESS_CODE).eq(ContractEntity::getDelFlag, "0")
                .orderByAsc(ContractEntity::getCreateTime).last("limit 1").one();
        if (c == null) throw new ServiceException("未找到零售融资租赁合同: " + contractCode);
        return c;
    }

    private String resolveEvent(String source, String scene, String eventField, String sceneName) {
        JSONObject mapping = new JSONObject();
        mapping.put("systemCode", SystemEnum.CYCXT.getCode());
        mapping.put("eventCode", source.trim());
        fieldMappingService.convertDataFromMapping(mapping);
        String event = mapping.getString(eventField);
        if (!scene.equals(mapping.getString("sceneCode")) || StrUtil.isBlank(event))
            throw new ServiceException(sceneName + "事件未配置值映射[eventCode]: " + source);
        return event;
    }

    private void validate(RetailLeasebackAdditionalEventDTO d, String scene, String event) {
        if (REFUND.equals(scene)) {
            positive(d.getRefundAmount(), "退款总金额[refundAmount]");
            equal(d.getRefundAmount(), d.getDepositAmount().add(d.getAdvanceReceiptAmount()).add(d.getUnidentifiedAmount()), "退款总金额必须等于各金额类型之和");
            if ("RF001".equals(event) && d.getAdvanceReceiptAmount().signum() <= 0) throw new ServiceException("普通退款必须填写预收款退款金额[advanceReceiptAmount]");
            if ("RF002".equals(event) && d.getDepositAmount().signum() <= 0) throw new ServiceException("保证金退款必须填写保证金金额[depositAmount]");
            if ("RF003".equals(event) && d.getUnidentifiedAmount().signum() <= 0) throw new ServiceException("未确认款退款必须填写未确认金额[unidentifiedAmount]");
        } else if (SUBSIDY.equals(scene)) {
            if (d.getSubsidyAmount().add(d.getSubsidyTaxAmount()).signum() <= 0) throw new ServiceException("贴息确认金额必须大于0");
        } else if (OVERDUE.equals(scene)) {
            if ("OD001".equals(event)) positive(d.getOverduePrincipalAmount(), "逾期本金[overduePrincipalAmount]");
            else if ("OD002".equals(event)) positive(d.getOverdueInterestAmount().add(d.getOverdueInterestTaxAmount()), "逾期利息及税额");
            else if ("OD003".equals(event)) positive(d.getOverdueResidualValueAmount().add(d.getOverdueResidualValueTaxAmount()), "逾期留购价及税额");
            else if ("OD004".equals(event)) positive(d.getPenaltyInterestAmount().add(d.getPenaltyInterestTaxAmount()), "罚息及税额");
            else throw new ServiceException("不支持的逾期事件: " + event);
        } else if (STRUCTURE.equals(scene)) {
            BigDecimal total = abs(d.getPrincipalAdjustmentAmount()).add(abs(d.getInterestAdjustmentAmount()))
                    .add(abs(d.getInterestTaxAdjustmentAmount())).add(abs(d.getResidualValueAdjustmentAmount()))
                    .add(abs(d.getResidualValueTaxAdjustmentAmount())).add(abs(d.getGpsAdjustmentAmount()));
            if (total.signum() <= 0) throw new ServiceException("交易结构变更至少填写一个非零调整金额");
        } else if (AUXILIARY.equals(scene)) {
            BigDecimal total;
            if ("AA001".equals(event)) {
                total = abs(d.getPrincipalBalance()).add(abs(d.getInterestBalance())).add(abs(d.getResidualValueBalance()))
                        .add(abs(d.getInterestTaxBalance())).add(abs(d.getResidualValueTaxBalance()))
                        .add(abs(d.getAccruedInterestBalance())).add(abs(d.getAccruedResidualValueBalance()))
                        .add(abs(d.getAccruedInterestTaxBalance())).add(abs(d.getAccruedResidualValueTaxBalance()));
            } else if ("AA002".equals(event)) {
                total = abs(d.getUnidentifiedReceiptAssistAmount()).add(abs(d.getManagementFeePayableAssistAmount()))
                        .add(abs(d.getInputVatReceivableAssistAmount())).add(abs(d.getUnearnedInterestAssistAmount()));
            } else throw new ServiceException("不支持的辅助账调整事件: " + event);
            positive(total, "辅助账调整金额合计");
        } else if (OTHER.equals(scene)) {
            if ("OT001".equals(event) || "OT002".equals(event)) positive(d.getTransferAmount(), "调拨金额[transferAmount]");
            else if ("OT003".equals(event) || "OT004".equals(event)) positive(d.getQuarterlyInterestAmount(), "季度结息金额[quarterlyInterestAmount]");
            else if ("OT005".equals(event)) positive(d.getOverpaidProfitSharingAmount(), "多支付分润费[overpaidProfitSharingAmount]");
            else if ("OT006".equals(event)) positive(d.getChannelShareAmount(), "渠道商分成金额[channelShareAmount]");
            else if ("OT007".equals(event)) positive(d.getPenaltyShareAdjustmentAmount(), "违约金分成调整金额[penaltyShareAdjustmentAmount]");
            else if ("OT008".equals(event)) {
                BigDecimal debit = d.getPromotionInterestAmount().add(d.getPromotionInputVatAmount());
                BigDecimal credit = d.getPromotionIncomeAmount().add(d.getPromotionProfitSharingAmount());
                positive(debit, "促销补差借方金额合计");
                equal(debit, credit, "促销补差借贷金额必须相等");
            } else throw new ServiceException("不支持的其他事件: " + event);
        }
    }

    private void validateBankAccount(String bankNo, ContractEntity contract) {
        if (StrUtil.isBlank(bankNo)) throw new ServiceException("退款事件必须提供付款银行账号[bankAccountNo]");
        List<BankAccountEntity> accounts = bankAccountService.selectBankAccountEntity(bankNo);
        if (CollectionUtil.isEmpty(accounts)) throw new ServiceException("银行账号未同步或已停用: " + bankNo);
        BankAccountEntity a = accounts.get(0);
        if (StrUtil.isBlank(a.getAccountCode()) || StrUtil.isBlank(a.getAccountName())) throw new ServiceException("银行账号未关联金蝶会计科目: " + bankNo);
        if (!StrUtil.equals(contract.getOrgId(), a.getOrgId())) throw new ServiceException("银行账号与合同签约主体不一致: " + bankNo);
    }

    private Map<String, Object> toRuleData(RetailLeasebackAdditionalEventDTO d, ContractEntity c, String scene,
                                            String sceneName, String eventField, String event) {
        Map<String, Object> m = new HashMap<>();
        String sourceEventValue = d.getEventCode().trim();
        String eventDisplayName = RetailLeasebackEventNames.displayName(event, sourceEventValue);
        m.put("systemCode", SystemEnum.CYCXT.getCode()); m.put("sourceSystemCode", "RETAIL_FINANCE_LEASE");
        m.put("systemName", "零售融资租赁业务系统"); m.put("businessCode", BUSINESS_CODE);
        m.put("businessName", "融资租赁业务-回租-乘用车"); m.put("sceneCode", scene);
        m.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL, sceneName); m.put("sceneName", sceneName);
        m.put("sourceEventOriginal", sourceEventValue); m.put("sourceEventCode", eventDisplayName);
        m.put("eventCode", event); m.put(eventField, event);
        m.put("orderId", d.getOrderId()); m.put("businessDate", d.getBusinessDate()); m.put("financeDate", d.getBusinessDate());
        m.put("contractId", c.getId()); m.put("contractCode", c.getContractCode()); m.put("contractName", c.getContractName());
        m.put("clientCode", c.getClientCode()); m.put("clientName", c.getClientName()); m.put("orgId", c.getOrgId());
        m.put("orgName", "华夏金融租赁有限公司");
        m.put("bankNo", StrUtil.blankToDefault(d.getBankAccountNo(), d.getTargetBankAccountNo())); m.put("currency", d.getCurrency());
        m.put("counterpartyName", d.getCounterpartyName()); m.put("transactionSerial", d.getTransactionSerial());
        m.put("changeReason", d.getChangeReason()); m.put("remark", d.getRemark());
        m.put("refundAmount", d.getRefundAmount()); m.put("depositAmount", d.getDepositAmount());
        m.put("advanceReceiptAmount", d.getAdvanceReceiptAmount()); m.put("unidentifiedAmount", d.getUnidentifiedAmount());
        m.put("subsidyAmount", d.getSubsidyAmount()); m.put("subsidyTaxAmount", d.getSubsidyTaxAmount());
        m.put("overduePrincipalAmount", d.getOverduePrincipalAmount()); m.put("overdueInterestAmount", d.getOverdueInterestAmount());
        m.put("overdueInterestTaxAmount", d.getOverdueInterestTaxAmount()); m.put("overdueResidualValueAmount", d.getOverdueResidualValueAmount());
        m.put("overdueResidualValueTaxAmount", d.getOverdueResidualValueTaxAmount()); m.put("penaltyInterestAmount", d.getPenaltyInterestAmount());
        m.put("penaltyInterestTaxAmount", d.getPenaltyInterestTaxAmount()); m.put("principalAdjustmentAmount", d.getPrincipalAdjustmentAmount());
        m.put("interestAdjustmentAmount", d.getInterestAdjustmentAmount()); m.put("interestTaxAdjustmentAmount", d.getInterestTaxAdjustmentAmount());
        m.put("residualValueAdjustmentAmount", d.getResidualValueAdjustmentAmount()); m.put("residualValueTaxAdjustmentAmount", d.getResidualValueTaxAdjustmentAmount());
        m.put("gpsAdjustmentAmount", d.getGpsAdjustmentAmount()); m.put("approvalRequired", false); m.put("approvalStatus", "NOT_REQUIRED");
        m.put("principalBalance", d.getPrincipalBalance()); m.put("interestBalance", d.getInterestBalance());
        m.put("residualValueBalance", d.getResidualValueBalance()); m.put("interestTaxBalance", d.getInterestTaxBalance());
        m.put("residualValueTaxBalance", d.getResidualValueTaxBalance()); m.put("accruedInterestBalance", d.getAccruedInterestBalance());
        m.put("accruedResidualValueBalance", d.getAccruedResidualValueBalance()); m.put("accruedInterestTaxBalance", d.getAccruedInterestTaxBalance());
        m.put("accruedResidualValueTaxBalance", d.getAccruedResidualValueTaxBalance());
        m.put("unidentifiedReceiptAssistAmount", d.getUnidentifiedReceiptAssistAmount());
        m.put("managementFeePayableAssistAmount", d.getManagementFeePayableAssistAmount());
        m.put("inputVatReceivableAssistAmount", d.getInputVatReceivableAssistAmount());
        m.put("unearnedInterestAssistAmount", d.getUnearnedInterestAssistAmount());
        m.put("originalClientCode", d.getOriginalClientCode()); m.put("originalClientName", d.getOriginalClientName());
        m.put("sourceBankAccountNo", d.getSourceBankAccountNo()); m.put("targetBankAccountNo", d.getTargetBankAccountNo());
        m.put("transferAmount", d.getTransferAmount()); m.put("quarterlyInterestAmount", d.getQuarterlyInterestAmount());
        m.put("overpaidProfitSharingAmount", d.getOverpaidProfitSharingAmount()); m.put("channelShareAmount", d.getChannelShareAmount());
        m.put("channelShareNetAmount", d.getChannelShareAmount().divide(new BigDecimal("1.06"), 2, java.math.RoundingMode.HALF_UP));
        m.put("channelShareTaxAmount", d.getChannelShareAmount().subtract((BigDecimal) m.get("channelShareNetAmount")));
        m.put("penaltyShareAdjustmentAmount", d.getPenaltyShareAdjustmentAmount());
        m.put("promotionInterestAmount", d.getPromotionInterestAmount()); m.put("promotionInputVatAmount", d.getPromotionInputVatAmount());
        m.put("promotionIncomeAmount", d.getPromotionIncomeAmount()); m.put("promotionProfitSharingAmount", d.getPromotionProfitSharingAmount());
        // JYJGBG is an established engine scene. Its contract-update hook expects every legacy
        // amount to be non-null, so the new typed adjustments also populate that compatibility contract.
        BigDecimal leaseAdjustment = d.getPrincipalAdjustmentAmount().add(d.getInterestAdjustmentAmount())
                .add(d.getInterestTaxAdjustmentAmount()).add(d.getResidualValueAdjustmentAmount())
                .add(d.getResidualValueTaxAdjustmentAmount());
        m.put("receivableLeaseAdjustAmount", leaseAdjustment); m.put("payableDeviceAdjustAmount", d.getGpsAdjustmentAmount());
        m.put("firstAdjustAmount", BigDecimal.ZERO); m.put("procedureAdjustRevenues", BigDecimal.ZERO);
        m.put("insuranceAdjustAmount", BigDecimal.ZERO); m.put("implementMarginAdjustAmount", BigDecimal.ZERO);
        m.put("residualAdjustAmount", BigDecimal.ZERO); m.put("otherAdjustRevenues", BigDecimal.ZERO);
        m.put("receivable_unconfirm_receipt", BigDecimal.ZERO); m.put("service_revenue", BigDecimal.ZERO);
        m.put("firmAdjustRebate", BigDecimal.ZERO); m.put("otherCostAdjustAmount", BigDecimal.ZERO);
        m.put("channelAdjustExpense", BigDecimal.ZERO); m.put("innerAdjustExpense", BigDecimal.ZERO);
        m.put("serviceAdjustAmount", BigDecimal.ZERO); m.put("marginAdjustAmount", BigDecimal.ZERO);
        m.put("payableBraceletAdjustAmount", BigDecimal.ZERO); m.put("receivableLeaseAmount", BigDecimal.ZERO);
        m.put("payableDeviceAmount", BigDecimal.ZERO); m.put("receivablePrincipalAdjustAmount", BigDecimal.ZERO);
        m.put("receivableInterestAdjustAmount", BigDecimal.ZERO); m.put("deratePrincipalAmount", BigDecimal.ZERO);
        m.put("derateInterestAmount", BigDecimal.ZERO); m.put("vendorProcedureAdjustRevenues", BigDecimal.ZERO);
        m.put("source_system", "RETAIL_FINANCE_LEASE"); m.put("event_name", eventDisplayName);
        m.put("customer_no", c.getClientCode()); m.put("customer_name", c.getClientName());
        m.put("contract_no", c.getContractCode()); m.put("contract_name", c.getContractName()); m.put("business_date", d.getBusinessDate());
        return m;
    }

    private void normalize(RetailLeasebackAdditionalEventDTO d) {
        d.setCurrency(StrUtil.blankToDefault(d.getCurrency(), "CNY"));
        d.setRefundAmount(zero(d.getRefundAmount())); d.setDepositAmount(zero(d.getDepositAmount()));
        d.setAdvanceReceiptAmount(zero(d.getAdvanceReceiptAmount())); d.setUnidentifiedAmount(zero(d.getUnidentifiedAmount()));
        d.setSubsidyAmount(zero(d.getSubsidyAmount())); d.setSubsidyTaxAmount(zero(d.getSubsidyTaxAmount()));
        d.setOverduePrincipalAmount(zero(d.getOverduePrincipalAmount())); d.setOverdueInterestAmount(zero(d.getOverdueInterestAmount()));
        d.setOverdueInterestTaxAmount(zero(d.getOverdueInterestTaxAmount())); d.setOverdueResidualValueAmount(zero(d.getOverdueResidualValueAmount()));
        d.setOverdueResidualValueTaxAmount(zero(d.getOverdueResidualValueTaxAmount())); d.setPenaltyInterestAmount(zero(d.getPenaltyInterestAmount()));
        d.setPenaltyInterestTaxAmount(zero(d.getPenaltyInterestTaxAmount())); d.setPrincipalAdjustmentAmount(zero(d.getPrincipalAdjustmentAmount()));
        d.setInterestAdjustmentAmount(zero(d.getInterestAdjustmentAmount())); d.setInterestTaxAdjustmentAmount(zero(d.getInterestTaxAdjustmentAmount()));
        d.setResidualValueAdjustmentAmount(zero(d.getResidualValueAdjustmentAmount())); d.setResidualValueTaxAdjustmentAmount(zero(d.getResidualValueTaxAdjustmentAmount()));
        d.setGpsAdjustmentAmount(zero(d.getGpsAdjustmentAmount()));
        d.setPrincipalBalance(zero(d.getPrincipalBalance())); d.setInterestBalance(zero(d.getInterestBalance()));
        d.setResidualValueBalance(zero(d.getResidualValueBalance())); d.setInterestTaxBalance(zero(d.getInterestTaxBalance()));
        d.setResidualValueTaxBalance(zero(d.getResidualValueTaxBalance())); d.setAccruedInterestBalance(zero(d.getAccruedInterestBalance()));
        d.setAccruedResidualValueBalance(zero(d.getAccruedResidualValueBalance())); d.setAccruedInterestTaxBalance(zero(d.getAccruedInterestTaxBalance()));
        d.setAccruedResidualValueTaxBalance(zero(d.getAccruedResidualValueTaxBalance()));
        d.setUnidentifiedReceiptAssistAmount(zero(d.getUnidentifiedReceiptAssistAmount()));
        d.setManagementFeePayableAssistAmount(zero(d.getManagementFeePayableAssistAmount()));
        d.setInputVatReceivableAssistAmount(zero(d.getInputVatReceivableAssistAmount()));
        d.setUnearnedInterestAssistAmount(zero(d.getUnearnedInterestAssistAmount()));
        d.setTransferAmount(zero(d.getTransferAmount())); d.setQuarterlyInterestAmount(zero(d.getQuarterlyInterestAmount()));
        d.setOverpaidProfitSharingAmount(zero(d.getOverpaidProfitSharingAmount())); d.setChannelShareAmount(zero(d.getChannelShareAmount()));
        d.setPenaltyShareAdjustmentAmount(zero(d.getPenaltyShareAdjustmentAmount())); d.setPromotionInterestAmount(zero(d.getPromotionInterestAmount()));
        d.setPromotionInputVatAmount(zero(d.getPromotionInputVatAmount())); d.setPromotionIncomeAmount(zero(d.getPromotionIncomeAmount()));
        d.setPromotionProfitSharingAmount(zero(d.getPromotionProfitSharingAmount()));
    }

    private static BigDecimal zero(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
    private static BigDecimal abs(BigDecimal v) { return v.abs(); }
    private static void positive(BigDecimal v, String name) { if (v.signum() <= 0) throw new ServiceException(name + "必须大于0"); }
    private static void equal(BigDecimal a, BigDecimal b, String message) { if (a.compareTo(b) != 0) throw new ServiceException(message); }
}
