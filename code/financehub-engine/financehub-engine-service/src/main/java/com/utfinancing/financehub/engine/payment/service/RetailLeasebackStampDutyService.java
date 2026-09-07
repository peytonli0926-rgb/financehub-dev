package com.utfinancing.financehub.engine.payment.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 起租成功后自动生成计提税金-印花税内部接口事件。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetailLeasebackStampDutyService {

    public static final String STAMP_DUTY_SCENE = "CYC_TAX_ACCRUAL";
    private static final String BUSINESS_CODE = RetailLeasebackPaymentService.BUSINESS_CODE;

    private final IRawTransactionDataService rawTransactionDataService;
    private final ITaxRateService taxRateService;
    @Lazy
    @Resource
    private IRuleService ruleService;

    public void triggerAfterLeaseStart(Map<String, Object> leaseStartData) {
        if (!isRetailPassengerLeaseback(leaseStartData)) return;
        String triggerOrderId = value(leaseStartData, "orderId", "sourceOrderId");
        String contractCode = value(leaseStartData, "contractCode", "contract_no");
        if (StrUtil.isBlank(triggerOrderId) || StrUtil.isBlank(contractCode)) return;
        String orderId = triggerOrderId + "-AUTO-JTYS";
        boolean exists = rawTransactionDataService.lambdaQuery()
                .eq(RawTransactionDataEntity::getOrderId, orderId)
                .eq(RawTransactionDataEntity::getDelFlag, "0").exists();
        if (exists) return;

        BigDecimal taxBase = resolveStampDutyTaxBase(leaseStartData);
        if (taxBase == null || taxBase.signum() <= 0) {
            throw new ServiceException("起租自动计提印花税缺少有效合同金额: " + contractCode);
        }
        BigDecimal rate = taxRateService.getValidTaxRateByCode(BUSINESS_CODE, "stamp_duty");
        if (rate == null || rate.signum() <= 0) {
            throw new ServiceException("未配置乘用车回租印花税率，业务类型=" + BUSINESS_CODE);
        }
        BigDecimal stampDutyAmount = taxBase.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        Map<String, Object> data = new HashMap<>();
        data.put("systemCode", "CYCXT");
        data.put("sourceSystemCode", "RETAIL_FINANCE_LEASE");
        data.put("systemName", "零售融资租赁业务系统");
        data.put("businessCode", BUSINESS_CODE);
        data.put("businessName", "融资租赁业务-回租-乘用车");
        data.put("sceneCode", STAMP_DUTY_SCENE);
        data.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL, "计提税金-印花税");
        data.put("sceneName", "计提税金-印花税");
        data.put("eventCode", "计提税金-印花税");
        data.put("orderId", orderId);
        Object businessDate = object(leaseStartData, "businessDate", "business_date");
        data.put("businessDate", businessDate);
        data.put("financeDate", businessDate);
        data.put("contractCode", contractCode);
        data.put("clientCode", value(leaseStartData, "clientCode", "customer_no"));
        data.put("clientName", value(leaseStartData, "clientName", "customer_name"));
        data.put("orgId", value(leaseStartData, "orgId", "accounting_org_code"));
        data.put("orgName", value(leaseStartData, "orgName", "accounting_org_name"));
        data.put("currency", valueOrDefault(leaseStartData, "CNY", "currencyType", "currency"));
        data.put("stampDutyTaxBase", taxBase);
        data.put("stampDutyAmount", stampDutyAmount);
        data.put("triggerOrderId", triggerOrderId);
        data.put("approvalRequired", false);
        data.put("approvalStatus", "NOT_REQUIRED");
        data.put("source_system", "CYCXT");
        data.put("triggerSourceSystem", value(leaseStartData, "systemCode", "source_system"));
        data.put("event_name", "计提税金-印花税");
        data.put("customer_no", data.get("clientCode"));
        data.put("customer_name", data.get("clientName"));
        data.put("contract_no", contractCode);
        data.put("business_date", businessDate);

        RawTransactionDataEntity raw = rawTransactionDataService.saveRawData(data);
        rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.RUNNING.getCode(), null);
        data.put("interfaceId", raw.getId());
        data.put("interfaceCreateTime", raw.getCreateTime());
        try {
            ruleService.executeRule(data);
            rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.SUCCESS.getCode(), null);
        } catch (RuntimeException ex) {
            rawTransactionDataService.updateStatus(raw.getId(), RawMessageStatusEnum.FAILED.getCode(), ex.getMessage());
            throw ex;
        }
    }

    /** 部署后补齐历史起租但尚未生成印花税的合同。 */
    @EventListener(ApplicationReadyEvent.class)
    public void backfillMissingStampDutyEvents() {
        List<RawTransactionDataEntity> starts = rawTransactionDataService.lambdaQuery()
                .eq(RawTransactionDataEntity::getSceneCode, "HTQZ")
                .eq(RawTransactionDataEntity::getMessageStatus, RawMessageStatusEnum.SUCCESS.getCode())
                .eq(RawTransactionDataEntity::getDelFlag, "0").list();
        for (RawTransactionDataEntity start : starts) {
            try {
                JSONObject content = start.getMessageContent();
                if (content == null) continue;
                Map<String, Object> data = content.to(new TypeReference<Map<String, Object>>() {});
                data.putIfAbsent("orderId", start.getOrderId());
                data.putIfAbsent("contractCode", start.getContractCode());
                data.putIfAbsent("businessDate", start.getBusinessDate());
                triggerAfterLeaseStart(data);
            } catch (Exception ex) {
                log.error("补生成起租印花税事件失败，起租单号={}", start.getOrderId(), ex);
            }
        }
    }

    private boolean isRetailPassengerLeaseback(Map<String, Object> data) {
        String system = value(data, "systemCode", "source_system");
        String line = value(data, "businessPlate", "business_line");
        String method = value(data, "returnType", "leaseMethod", "lease_method");
        return ("RETAIL_FINANCE_LEASE".equals(system) || "CYCXT".equals(system))
                && "乘用车".equals(line) && "回租".equals(method);
    }

    private static Object object(Map<String, Object> data, String... keys) {
        for (String key : keys) if (data.get(key) != null) return data.get(key);
        return null;
    }
    private static String value(Map<String, Object> data, String... keys) {
        Object result = object(data, keys);
        return result == null ? null : String.valueOf(result);
    }
    private static String valueOrDefault(Map<String, Object> data, String defaultValue, String... keys) {
        String result = value(data, keys);
        return StrUtil.isBlank(result) ? defaultValue : result;
    }
    private static BigDecimal decimal(Map<String, Object> data, String... keys) {
        String result = value(data, keys);
        return StrUtil.isBlank(result) ? null : new BigDecimal(result);
    }

    /** 融资租赁印花税以租金总额计税，不包含留购价。 */
    @SuppressWarnings("unchecked")
    private static BigDecimal resolveStampDutyTaxBase(Map<String, Object> data) {
        Object planValue = object(data, "repaymentPlan", "repayment_plan");
        if (planValue instanceof List) {
            BigDecimal rentTotal = BigDecimal.ZERO;
            for (Object item : (List<?>) planValue) {
                if (!(item instanceof Map)) continue;
                BigDecimal rent = decimal((Map<String, Object>) item, "rentAmount", "rent_amount");
                if (rent != null) rentTotal = rentTotal.add(rent);
            }
            if (rentTotal.signum() > 0) return rentTotal;
        }
        BigDecimal total = decimal(data, "totalLeaseReceivable", "total_lease_receivable", "contractAmount", "contract_amount");
        BigDecimal residual = decimal(data, "retainedPrice", "residualValue", "residual_value");
        return total == null ? null : total.subtract(residual == null ? BigDecimal.ZERO : residual);
    }
}
