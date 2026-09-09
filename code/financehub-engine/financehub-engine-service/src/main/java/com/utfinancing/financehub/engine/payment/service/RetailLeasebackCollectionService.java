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
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackCollectionDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 将业务系统的多种收款事件统一转换成财务中台“收款”场景。 */
@Service
@RequiredArgsConstructor
public class RetailLeasebackCollectionService {
    public static final String SCENE_CODE = "CYC_COLLECTION";
    private static final String BUSINESS_CODE = RetailLeasebackPaymentService.BUSINESS_CODE;
    private static final Set<String> TRANSFER_EVENTS = set("CR007", "CR008");

    private final IRuleService ruleService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final IFieldMappingService fieldMappingService;
    private final IContractService contractService;
    private final IBankAccountService bankAccountService;

    public List<VoucherDTO> execute(RetailLeasebackCollectionDTO dto) {
        normalize(dto);
        if (rawTransactionDataService.lambdaQuery().eq(RawTransactionDataEntity::getOrderId, dto.getOrderId())
                .eq(RawTransactionDataEntity::getDelFlag, "0").exists()) {
            throw new ServiceException("请求流水号已存在: " + dto.getOrderId());
        }
        ContractEntity contract = resolveContract(dto.getContractCode());
        String collectionEventCode = resolveEventCode(dto.getEventCode());
        validateAmounts(dto, collectionEventCode);
        if (!TRANSFER_EVENTS.contains(collectionEventCode)) validateBankAccount(dto.getBankAccountNo(), contract);

        Map<String, Object> data = toRuleData(dto, contract, collectionEventCode);
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

    private void normalize(RetailLeasebackCollectionDTO d) {
        d.setReceivedAmount(zero(d.getReceivedAmount())); d.setApplicationAmount(zero(d.getApplicationAmount()));
        d.setCashDiscountAmount(zero(d.getCashDiscountAmount())); d.setDepositAmount(zero(d.getDepositAmount()));
        d.setUnidentifiedAmount(zero(d.getUnidentifiedAmount())); d.setPrincipalAmount(zero(d.getPrincipalAmount()));
        d.setInterestAmount(zero(d.getInterestAmount())); d.setInterestTaxAmount(zero(d.getInterestTaxAmount()));
        d.setResidualValueAmount(zero(d.getResidualValueAmount())); d.setResidualValueTaxAmount(zero(d.getResidualValueTaxAmount()));
        d.setOverduePrincipalAmount(zero(d.getOverduePrincipalAmount())); d.setOverdueInterestAmount(zero(d.getOverdueInterestAmount()));
        d.setOverdueInterestTaxAmount(zero(d.getOverdueInterestTaxAmount())); d.setOverdueResidualValueAmount(zero(d.getOverdueResidualValueAmount()));
        d.setOverdueResidualValueTaxAmount(zero(d.getOverdueResidualValueTaxAmount())); d.setProfitSharingRefundAmount(zero(d.getProfitSharingRefundAmount()));
        d.setCurrency(StrUtil.blankToDefault(d.getCurrency(), "CNY"));
    }

    private ContractEntity resolveContract(String contractCode) {
        ContractEntity contract = contractService.lambdaQuery().eq(ContractEntity::getContractCode, contractCode)
                .eq(ContractEntity::getBusinessCode, BUSINESS_CODE).eq(ContractEntity::getDelFlag, "0")
                .orderByAsc(ContractEntity::getCreateTime).last("limit 1").one();
        if (contract == null) throw new ServiceException("未找到零售融资租赁合同: " + contractCode);
        return contract;
    }

    private String resolveEventCode(String sourceEventCode) {
        JSONObject mapping = new JSONObject();
        mapping.put("systemCode", SystemEnum.CYCXT.getCode());
        mapping.put("eventCode", sourceEventCode.trim());
        fieldMappingService.convertDataFromMapping(mapping);
        String eventCode = mapping.getString("collectionEventCode");
        if (!SCENE_CODE.equals(mapping.getString("sceneCode")) || StrUtil.isBlank(eventCode))
            throw new ServiceException("收款事件未配置值映射[eventCode]: " + sourceEventCode);
        return eventCode;
    }

    private void validateAmounts(RetailLeasebackCollectionDTO d, String event) {
        Map<String, BigDecimal> amounts = allocationAmounts(d);
        BigDecimal total = amounts.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (TRANSFER_EVENTS.contains(event)) {
            requirePositive(d.getApplicationAmount(), "核销金额[applicationAmount]");
            requireZero(d.getReceivedAmount(), "转账核销事件不允许填写实际收款金额[receivedAmount]");
            requireEqual(d.getApplicationAmount(), total, event + "要求applicationAmount等于各核销金额之和");
        } else {
            requirePositive(d.getReceivedAmount(), "实际收款金额[receivedAmount]");
            requireZero(d.getApplicationAmount(), "银行到账事件不允许填写核销金额[applicationAmount]");
            requireEqual(d.getReceivedAmount(), total, event + "要求receivedAmount等于各收款金额之和");
        }
        validateAllowed(event, amounts);
    }

    private void validateAllowed(String event, Map<String, BigDecimal> amounts) {
        Set<String> allowed;
        switch (event) {
            case "CR000": allowed = set("cashDiscountAmount"); break;
            case "CR001": allowed = set("depositAmount"); break;
            case "CR005": allowed = set("principalAmount", "interestAmount", "interestTaxAmount", "residualValueAmount", "residualValueTaxAmount"); break;
            case "CR006": allowed = set("unidentifiedAmount"); break;
            case "CR007":
            case "CR008": allowed = set("depositAmount", "principalAmount", "interestAmount", "interestTaxAmount", "residualValueAmount", "residualValueTaxAmount", "overduePrincipalAmount", "overdueInterestAmount", "overdueInterestTaxAmount", "overdueResidualValueAmount", "overdueResidualValueTaxAmount", "profitSharingRefundAmount"); break;
            case "CR033": allowed = set("profitSharingRefundAmount"); break;
            case "CR043": allowed = set("overdueResidualValueAmount", "overdueResidualValueTaxAmount"); break;
            default: throw new ServiceException("不支持的财务中台收款事件: " + event);
        }
        amounts.forEach((field, amount) -> {
            if (amount.signum() > 0 && !allowed.contains(field)) throw new ServiceException(event + "不允许填写金额字段: " + field);
        });
    }

    private Map<String, BigDecimal> allocationAmounts(RetailLeasebackCollectionDTO d) {
        Map<String, BigDecimal> v = new HashMap<>();
        v.put("cashDiscountAmount", d.getCashDiscountAmount()); v.put("depositAmount", d.getDepositAmount());
        v.put("unidentifiedAmount", d.getUnidentifiedAmount()); v.put("principalAmount", d.getPrincipalAmount());
        v.put("interestAmount", d.getInterestAmount()); v.put("interestTaxAmount", d.getInterestTaxAmount());
        v.put("residualValueAmount", d.getResidualValueAmount()); v.put("residualValueTaxAmount", d.getResidualValueTaxAmount());
        v.put("overduePrincipalAmount", d.getOverduePrincipalAmount()); v.put("overdueInterestAmount", d.getOverdueInterestAmount());
        v.put("overdueInterestTaxAmount", d.getOverdueInterestTaxAmount()); v.put("overdueResidualValueAmount", d.getOverdueResidualValueAmount());
        v.put("overdueResidualValueTaxAmount", d.getOverdueResidualValueTaxAmount()); v.put("profitSharingRefundAmount", d.getProfitSharingRefundAmount());
        return v;
    }

    private void validateBankAccount(String bankNo, ContractEntity contract) {
        if (StrUtil.isBlank(bankNo)) throw new ServiceException("银行到账事件必须提供到账银行账号[bankAccountNo]");
        List<BankAccountEntity> accounts = bankAccountService.selectBankAccountEntity(bankNo);
        if (CollectionUtil.isEmpty(accounts)) throw new ServiceException("银行账号未同步或已停用: " + bankNo);
        BankAccountEntity account = accounts.get(0);
        if (StrUtil.isBlank(account.getAccountCode()) || StrUtil.isBlank(account.getAccountName()))
            throw new ServiceException("银行账号未关联金蝶会计科目: " + bankNo);
        if (!StrUtil.equals(contract.getOrgId(), account.getOrgId()))
            throw new ServiceException("到账银行账号与合同签约主体不一致: " + bankNo);
    }

    private Map<String, Object> toRuleData(RetailLeasebackCollectionDTO d, ContractEntity c, String event) {
        Map<String, Object> data = new HashMap<>();
        String sourceEventValue = d.getEventCode().trim();
        String eventDisplayName = RetailLeasebackEventNames.displayName(event, sourceEventValue);
        data.put("systemCode", SystemEnum.CYCXT.getCode()); data.put("sourceSystemCode", "RETAIL_FINANCE_LEASE");
        data.put("systemName", "零售融资租赁业务系统"); data.put("businessCode", BUSINESS_CODE);
        data.put("businessName", "融资租赁业务-回租-乘用车"); data.put("sceneCode", SCENE_CODE);
        data.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL, "收款"); data.put("sceneName", "收款");
        data.put("sourceEventOriginal", sourceEventValue); data.put("sourceEventCode", eventDisplayName);
        data.put("eventCode", event); data.put("collectionEventCode", event);
        data.put("orderId", d.getOrderId()); data.put("businessDate", d.getBusinessDate()); data.put("financeDate", d.getBusinessDate());
        data.put("contractId", c.getId()); data.put("contractCode", c.getContractCode()); data.put("contractName", c.getContractName());
        data.put("clientCode", c.getClientCode()); data.put("clientName", c.getClientName()); data.put("orgId", c.getOrgId());
        data.put("orgName", "华夏金融租赁有限公司"); data.put("bankNo", d.getBankAccountNo()); data.put("currency", d.getCurrency());
        allocationAmounts(d).forEach(data::put); data.put("receivedAmount", d.getReceivedAmount()); data.put("applicationAmount", d.getApplicationAmount());
        data.put("payerName", d.getPayerName()); data.put("transactionSerial", d.getTransactionSerial()); data.put("remark", d.getRemark());
        data.put("approvalRequired", false); data.put("approvalStatus", "NOT_REQUIRED");
        data.put("source_system", "RETAIL_FINANCE_LEASE"); data.put("event_name", eventDisplayName);
        data.put("customer_no", c.getClientCode()); data.put("customer_name", c.getClientName());
        data.put("contract_no", c.getContractCode()); data.put("contract_name", c.getContractName()); data.put("business_date", d.getBusinessDate());
        return data;
    }

    private static BigDecimal zero(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private static Set<String> set(String... values) { return new HashSet<>(Arrays.asList(values)); }
    private static void requirePositive(BigDecimal value, String field) { if (value.signum() <= 0) throw new ServiceException(field + "必须大于0"); }
    private static void requireZero(BigDecimal value, String message) { if (value.signum() != 0) throw new ServiceException(message); }
    private static void requireEqual(BigDecimal left, BigDecimal right, String message) { if (left.compareTo(right) != 0) throw new ServiceException(message); }
}
