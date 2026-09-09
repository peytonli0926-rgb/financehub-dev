package com.utfinancing.financehub.engine.payment.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackPaymentDTO;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 将零售回租付款接口转换成会计引擎统一规则报文。
 */
@Service
@RequiredArgsConstructor
public class RetailLeasebackPaymentService {

    public static final String BUSINESS_CODE = "CYC_RETAIL_LEASEBACK";
    public static final String PAYMENT_SCENE_CODE = "CYC_PAYMENT";
    private final IRuleService ruleService;
    private final IBankAccountService bankAccountService;
    private final IFieldMappingService fieldMappingService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final ITaxRateService taxRateService;
    private final IContractService contractService;

    public List<VoucherDTO> execute(RetailLeasebackPaymentDTO dto) {
        normalize(dto);
        normalizeContractDimensions(dto);
        JSONObject mappingData = new JSONObject();
        mappingData.put("systemCode", SystemEnum.CYCXT.getCode());
        mappingData.put("eventCode", dto.getEventCode());
        fieldMappingService.convertDataFromMapping(mappingData);
        String sceneCode = mappingData.getString("sceneCode");
        String paymentEventCode = mappingData.getString("paymentEventCode");
        if (!PAYMENT_SCENE_CODE.equals(sceneCode) || StrUtil.isBlank(paymentEventCode)) {
            throw new ServiceException("零售融资租赁业务系统事件未配置付款值映射[eventCode]: " + dto.getEventCode());
        }
        BigDecimal configuredTaxRate = resolveConfiguredTaxRate(paymentEventCode);
        normalizeEventAmounts(dto, paymentEventCode);
        validate(dto, paymentEventCode, configuredTaxRate);
        Map<String, Object> ruleData = toRuleData(dto, sceneCode, paymentEventCode, configuredTaxRate);
        RawTransactionDataEntity rawDocument = rawTransactionDataService.saveRawData(ruleData);
        rawTransactionDataService.updateStatus(rawDocument.getId(), RawMessageStatusEnum.RUNNING.getCode(), null);
        ruleData.put("interfaceId", rawDocument.getId());
        ruleData.put("interfaceCreateTime", rawDocument.getCreateTime());
        try {
            List<VoucherDTO> vouchers = ruleService.executeRule(ruleData);
            rawTransactionDataService.updateStatus(rawDocument.getId(), RawMessageStatusEnum.SUCCESS.getCode(), null);
            return vouchers;
        } catch (RuntimeException e) {
            rawTransactionDataService.updateStatus(rawDocument.getId(), RawMessageStatusEnum.FAILED.getCode(), e.getMessage());
            throw e;
        }
    }

    /**
     * 付款是既有合同的后续事件，合同、客户和签约主体必须沿用合同主数据。
     */
    private void normalizeContractDimensions(RetailLeasebackPaymentDTO dto) {
        ContractEntity contract = contractService.lambdaQuery()
                .eq(ContractEntity::getContractCode, dto.getContractCode())
                .eq(ContractEntity::getBusinessCode, BUSINESS_CODE)
                .eq(ContractEntity::getDelFlag, "0")
                .orderByAsc(ContractEntity::getCreateTime)
                .last("limit 1")
                .one();
        if (contract == null) {
            throw new ServiceException("未找到零售融资租赁合同: " + dto.getContractCode());
        }
        dto.setOrgId(contract.getOrgId());
        dto.setClientCode(contract.getClientCode());
        dto.setClientName(contract.getClientName());
    }

    private void normalize(RetailLeasebackPaymentDTO dto) {
        dto.setEventCode(dto.getEventCode().trim().toUpperCase(Locale.ROOT));
        if (StrUtil.isBlank(dto.getCurrency())) {
            dto.setCurrency("CNY");
        }
        if (StrUtil.isBlank(dto.getPaymentMethod())) {
            dto.setPaymentMethod("BANK_TRANSFER");
        } else {
            dto.setPaymentMethod(dto.getPaymentMethod().toUpperCase(Locale.ROOT));
        }
        if (StrUtil.isBlank(dto.getAssetStage())) {
            dto.setAssetStage("COMPLETED");
        } else {
            dto.setAssetStage(dto.getAssetStage().toUpperCase(Locale.ROOT));
        }
        dto.setUntaxedAmount(zero(dto.getUntaxedAmount()));
        dto.setInterestAmount(zero(dto.getInterestAmount()));
        dto.setInitialPaymentAmount(zero(dto.getInitialPaymentAmount()));
        dto.setRefundAmount(zero(dto.getRefundAmount()));
        dto.setOffsetAmount(zero(dto.getOffsetAmount()));
        dto.setBankPaymentAmount(zero(dto.getBankPaymentAmount()));
    }

    private void normalizeEventAmounts(RetailLeasebackPaymentDTO dto, String paymentEventCode) {
        if ("CR029".equals(paymentEventCode)
                && dto.getBankPaymentAmount().signum() == 0
                && dto.getRefundAmount().signum() == 0
                && dto.getOffsetAmount().signum() == 0) {
            dto.setBankPaymentAmount(dto.getPaymentAmount());
        }
    }

    private void validate(RetailLeasebackPaymentDTO dto, String paymentEventCode, BigDecimal configuredTaxRate) {
        requirePositive(dto.getPaymentAmount(), "付款总额[paymentAmount]");
        requireNonNegative(dto.getUntaxedAmount(), "不含税金额[untaxedAmount]");
        requireNonNegative(dto.getInterestAmount(), "不含税利息金额[interestAmount]");
        requireNonNegative(dto.getInitialPaymentAmount(), "首付款金额[initialPaymentAmount]");
        requireNonNegative(dto.getRefundAmount(), "应退分润费[refundAmount]");
        requireNonNegative(dto.getOffsetAmount(), "抵扣金额[offsetAmount]");
        requireNonNegative(dto.getBankPaymentAmount(), "银行实付金额[bankPaymentAmount]");

        if ("CR003".equals(paymentEventCode)) {
            if (!Arrays.asList("BANK_TRANSFER", "BANK_ACCEPTANCE").contains(dto.getPaymentMethod())) {
                throw new ServiceException("CR003支付方式仅支持BANK_TRANSFER或BANK_ACCEPTANCE");
            }
            if (!Arrays.asList("COMPLETED", "CONSTRUCTION").contains(dto.getAssetStage())) {
                throw new ServiceException("CR003资产状态仅支持COMPLETED或CONSTRUCTION");
            }
        }
        // CR025 的 paymentAmount 是实际银行支付的含税总额，不要求接口重复提供不含税金额。
        // 不含税管理费和待认证进项税均由业务类型配置的税率反算。
        if ("CR025".equals(paymentEventCode)) {
            requirePositive(calculateUntaxedAmount(dto.getPaymentAmount(), configuredTaxRate), "不含税资产管理费");
        }
        if (Arrays.asList("CR040", "CR041").contains(paymentEventCode)) {
            requirePositive(dto.getUntaxedAmount(), "不含税金额[untaxedAmount]");
            requireEqual(dto.getPaymentAmount(), dto.getUntaxedAmount().add(calculateTaxAmount(dto.getUntaxedAmount(), configuredTaxRate)),
                    paymentEventCode + "要求paymentAmount=untaxedAmount+untaxedAmount*配置税率");
        }
        if ("CR056".equals(paymentEventCode)) {
            requirePositive(dto.getInterestAmount(), "不含税利息金额[interestAmount]");
            requireEqual(dto.getPaymentAmount(), dto.getInterestAmount().add(calculateTaxAmount(dto.getInterestAmount(), configuredTaxRate)),
                    "CR056要求paymentAmount=interestAmount+interestAmount*配置税率");
        }
        if ("CR029".equals(paymentEventCode)) {
            BigDecimal detailTotal = dto.getRefundAmount().add(dto.getOffsetAmount()).add(dto.getBankPaymentAmount());
            requireEqual(dto.getPaymentAmount(), detailTotal,
                    "CR029要求paymentAmount=refundAmount+offsetAmount+bankPaymentAmount");
        }

        List<BankAccountEntity> bankAccounts = bankAccountService.selectBankAccountEntity(dto.getBankAccountNo());
        if (CollectionUtil.isEmpty(bankAccounts)) {
            throw new ServiceException("银行账号未同步或已停用: " + dto.getBankAccountNo());
        }
        BankAccountEntity account = bankAccounts.get(0);
        if (StrUtil.isBlank(account.getAccountCode()) || StrUtil.isBlank(account.getAccountName())) {
            throw new ServiceException("银行账号未关联金蝶会计科目: " + dto.getBankAccountNo());
        }
        if ("BANK_TRANSFER".equals(dto.getPaymentMethod()) && dto.getBankAccountNo() == null) {
            throw new ServiceException("银行转账必须提供银行账号");
        }
    }

    private Map<String, Object> toRuleData(RetailLeasebackPaymentDTO dto, String sceneCode, String paymentEventCode,
                                           BigDecimal configuredTaxRate) {
        Map<String, Object> data = new HashMap<>();
        String eventDisplayName = RetailLeasebackEventNames.displayName(paymentEventCode, dto.getEventCode());
        data.put("systemCode", SystemEnum.CYCXT.getCode());
        data.put("sourceSystemCode", "RETAIL_FINANCE_LEASE");
        data.put("systemName", "零售融资租赁业务系统");
        data.put("businessCode", BUSINESS_CODE);
        data.put("businessName", "融资租赁业务-回租-乘用车");
        // sceneCode 和内部事件码均来自值映射配置，接口事件值保持为来源系统原值。
        data.put("sceneCode", sceneCode);
        data.put("sceneName", "付款");
        data.put("sourceEventOriginal", dto.getEventCode());
        data.put("sourceEventCode", eventDisplayName);
        data.put("eventCode", paymentEventCode);
        data.put("paymentEventCode", paymentEventCode);
        data.put("orderId", dto.getOrderId());
        data.put("businessDate", dto.getBusinessDate());
        data.put("financeDate", dto.getBusinessDate());
        data.put("contractCode", dto.getContractCode());
        data.put("clientCode", dto.getClientCode());
        data.put("clientName", dto.getClientName());
        data.put("orgId", dto.getOrgId());
        data.put("orgName", dto.getOrgName());
        data.put("bankNo", dto.getBankAccountNo());
        data.put("currency", dto.getCurrency());
        data.put("paymentMethod", dto.getPaymentMethod());
        data.put("assetStage", dto.getAssetStage());
        data.put("paymentAmount", dto.getPaymentAmount());
        BigDecimal managementFeeAmount = "CR025".equals(paymentEventCode)
                ? calculateUntaxedAmount(dto.getPaymentAmount(), configuredTaxRate) : BigDecimal.ZERO;
        BigDecimal taxAmount = "CR025".equals(paymentEventCode)
                ? dto.getPaymentAmount().subtract(managementFeeAmount)
                : calculateTaxAmount("CR056".equals(paymentEventCode) ? dto.getInterestAmount() : dto.getUntaxedAmount(), configuredTaxRate);
        data.put("untaxedAmount", dto.getUntaxedAmount());
        data.put("managementFeeAmount", managementFeeAmount);
        data.put("paymentInterestAmount", dto.getInterestAmount());
        data.put("taxAmount", taxAmount);
        data.put("initialPaymentAmount", dto.getInitialPaymentAmount());
        data.put("refundAmount", dto.getRefundAmount());
        data.put("offsetAmount", dto.getOffsetAmount());
        data.put("bankPaymentAmount", dto.getBankPaymentAmount());
        data.put("payeeCode", dto.getPayeeCode());
        data.put("payeeName", dto.getPayeeName());
        data.put("remark", dto.getRemark());
        data.put("leaseMethod", "回租");
        // “我的单据”只依赖所有来源系统均可适配的通用业务字段。
        data.put("source_system", "RETAIL_FINANCE_LEASE");
        data.put("event_name", eventDisplayName);
        data.put("customer_no", dto.getClientCode());
        data.put("customer_name", dto.getClientName());
        data.put("contract_no", dto.getContractCode());
        data.put("business_date", dto.getBusinessDate());
        return data;
    }

    private BigDecimal resolveConfiguredTaxRate(String paymentEventCode) {
        if (!Arrays.asList("CR025", "CR040", "CR041", "CR056").contains(paymentEventCode)) {
            return null;
        }
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(BUSINESS_CODE, "tax_general");
        if (taxRate == null || taxRate.signum() <= 0) {
            throw new ServiceException("未配置有效税率，业务类型=" + BUSINESS_CODE);
        }
        return taxRate;
    }

    private static BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static BigDecimal calculateTaxAmount(BigDecimal amount, BigDecimal taxRate) {
        if (amount == null || taxRate == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateUntaxedAmount(BigDecimal taxInclusiveAmount, BigDecimal taxRate) {
        if (taxInclusiveAmount == null || taxRate == null) {
            return BigDecimal.ZERO;
        }
        return taxInclusiveAmount.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
    }

    private static void requirePositive(BigDecimal value, String field) {
        if (value == null || value.signum() <= 0) {
            throw new ServiceException(field + "必须大于0");
        }
    }

    private static void requireNonNegative(BigDecimal value, String field) {
        if (value.signum() < 0) {
            throw new ServiceException(field + "不能小于0");
        }
    }

    private static void requireEqual(BigDecimal left, BigDecimal right, String message) {
        if (left.compareTo(right) != 0) {
            throw new ServiceException(message);
        }
    }
}
