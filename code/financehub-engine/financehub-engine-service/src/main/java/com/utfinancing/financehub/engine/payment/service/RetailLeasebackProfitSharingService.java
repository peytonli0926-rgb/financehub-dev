package com.utfinancing.financehub.engine.payment.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackProfitSharingDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** 一个接口承接分润费正向确认和退回冲销两个事件。 */
@Service
@RequiredArgsConstructor
public class RetailLeasebackProfitSharingService {

    public static final String SCENE_CODE = "CYC_PROFIT_SHARING_CONFIRM";
    private static final String BUSINESS_CODE = RetailLeasebackPaymentService.BUSINESS_CODE;

    private final IRuleService ruleService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final IFieldMappingService fieldMappingService;
    private final IContractService contractService;

    public List<VoucherDTO> execute(RetailLeasebackProfitSharingDTO dto) {
        String inputEvent = dto.getEventCode().trim();
        String normalizedInput = inputEvent.toUpperCase(Locale.ROOT);
        if ("FRF_CONFIRM".equals(normalizedInput)) inputEvent = "FRF_CONFIRM";
        if ("FRF_REFUND".equals(normalizedInput)) inputEvent = "FRF_REFUND";

        boolean duplicate = rawTransactionDataService.lambdaQuery()
                .eq(RawTransactionDataEntity::getOrderId, dto.getOrderId())
                .eq(RawTransactionDataEntity::getDelFlag, "0").exists();
        if (duplicate) throw new ServiceException("请求流水号已存在: " + dto.getOrderId());

        ContractEntity contract = contractService.lambdaQuery()
                .eq(ContractEntity::getContractCode, dto.getContractCode())
                .eq(ContractEntity::getBusinessCode, BUSINESS_CODE)
                .eq(ContractEntity::getDelFlag, "0")
                .orderByAsc(ContractEntity::getCreateTime)
                .last("limit 1").one();
        if (contract == null) throw new ServiceException("未找到零售融资租赁合同: " + dto.getContractCode());

        JSONObject mapping = new JSONObject();
        mapping.put("systemCode", SystemEnum.CYCXT.getCode());
        mapping.put("eventCode", inputEvent);
        fieldMappingService.convertDataFromMapping(mapping);
        String sceneCode = mapping.getString("sceneCode");
        String internalEvent = mapping.getString("profitSharingEventCode");
        if (!SCENE_CODE.equals(sceneCode) || StrUtil.isBlank(internalEvent)) {
            throw new ServiceException("分润费事件未配置值映射: " + dto.getEventCode());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("systemCode", SystemEnum.CYCXT.getCode());
        data.put("sourceSystemCode", "RETAIL_FINANCE_LEASE");
        data.put("systemName", "零售融资租赁业务系统");
        data.put("businessCode", BUSINESS_CODE);
        data.put("businessName", "融资租赁业务-回租-乘用车");
        data.put("sceneCode", sceneCode);
        data.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL, "分润费确认");
        data.put("sceneName", "分润费确认");
        data.put("eventCode", internalEvent);
        data.put("profitSharingEventCode", internalEvent);
        data.put("orderId", dto.getOrderId());
        data.put("businessDate", dto.getBusinessDate());
        data.put("financeDate", dto.getBusinessDate());
        data.put("contractCode", contract.getContractCode());
        data.put("clientCode", contract.getClientCode());
        data.put("clientName", contract.getClientName());
        data.put("orgId", contract.getOrgId());
        data.put("orgName", "华夏金融租赁有限公司");
        data.put("currency", StrUtil.blankToDefault(dto.getCurrency(), "CNY"));
        data.put("profitSharingAmount", dto.getProfitSharingAmount());
        data.put("triggerOrderId", StrUtil.blankToDefault(dto.getTriggerOrderId(), "HTQZ-20260903-0001"));
        data.put("remark", dto.getRemark());
        data.put("approvalRequired", false);
        data.put("approvalStatus", "NOT_REQUIRED");
        data.put("source_system", "RETAIL_FINANCE_LEASE");
        data.put("event_name", "FRF_CONFIRM".equals(internalEvent) ? "起租日确认分润费" : "退回分润费");
        data.put("customer_no", contract.getClientCode());
        data.put("customer_name", contract.getClientName());
        data.put("contract_no", contract.getContractCode());
        data.put("business_date", dto.getBusinessDate());

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
}
