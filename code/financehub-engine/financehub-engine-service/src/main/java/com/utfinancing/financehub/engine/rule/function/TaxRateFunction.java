package com.utfinancing.financehub.engine.rule.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 18/09/2023
 * @Description Aviator自定义函数：查询税率配置
 */
@Component("taxRate")
public class TaxRateFunction extends AbstractFunction {

    @Resource
    private ITaxRateService taxRateService;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String businessCode = normalizeBusinessCode(FunctionUtils.getStringValue(arg1, env));
        String fundType = FunctionUtils.getStringValue(arg2, env);
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(businessCode, fundType);
        return decimalOrThrow(taxRate, businessCode, fundType, null, null);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2,
                              AviatorObject arg3, AviatorObject arg4) {
        String businessCode = normalizeBusinessCode(FunctionUtils.getStringValue(arg1, env));
        String fundType = FunctionUtils.getStringValue(arg2, env);
        String leaseType = normalizeLeaseType(FunctionUtils.getStringValue(arg3, env));
        String leaseMethod = normalizeLeaseMethod(FunctionUtils.getStringValue(arg4, env));
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(
                businessCode, fundType, leaseType, leaseMethod);
        return decimalOrThrow(taxRate, businessCode, fundType, leaseType, leaseMethod);
    }

    private AviatorObject decimalOrThrow(BigDecimal taxRate, String businessCode, String fundType,
                                         String leaseType, String leaseMethod) {
        if (taxRate == null) {
            throw new ServiceException("未配置有效税率，业务类型=" + businessCode
                    + "，金额类型=" + fundType + "，租赁类型=" + leaseType + "，租赁方式=" + leaseMethod);
        }
        return new AviatorDecimal(taxRate);
    }

    private String normalizeBusinessCode(String value) {
        if ("RETAIL_FINANCE_LEASE".equalsIgnoreCase(value)) {
            return "CYC_RETAIL_LEASEBACK";
        }
        if ("OPERATING_LEASE".equalsIgnoreCase(value)) {
            return "JYZL";
        }
        if ("FINANCE_LEASE".equalsIgnoreCase(value) || "HOUSEHOLD_PV".equalsIgnoreCase(value)) {
            return "ZLYW";
        }
        return value;
    }

    private String normalizeLeaseType(String value) {
        return "经营性租赁".equals(value) || "经营租赁".equals(value)
                || "OPERATING_LEASE".equalsIgnoreCase(value) ? "OPERATING_LEASE" : "FINANCE_LEASE";
    }

    private String normalizeLeaseMethod(String value) {
        return "回租".equals(value) || "LEASEBACK".equalsIgnoreCase(value)
                || "SALE_AND_LEASEBACK".equalsIgnoreCase(value) ? "SALE_AND_LEASEBACK" : "DIRECT_LEASE";
    }

    @Override
    public String getName() {
        return "taxRate";
    }
}
