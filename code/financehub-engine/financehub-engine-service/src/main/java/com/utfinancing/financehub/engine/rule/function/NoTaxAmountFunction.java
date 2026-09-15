package com.utfinancing.financehub.engine.rule.function;

import cn.hutool.core.util.NumberUtil;
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
 * @Description Aviator自定义函数：计算不含税金额
 * 参数：（含税金额， 业务编码， 金额类型）
 * 返回：不含税金额(保留两位小数)
 * 计算公式： 不含税金额 = 含税金额/(1+税率)
 */
@Component("noTaxAmount")
public class NoTaxAmountFunction extends AbstractFunction {

    @Resource
    private ITaxRateService taxRateService;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        Number amount = FunctionUtils.getNumberValue(arg1, env);
        String businessCode = normalizeBusinessCode(FunctionUtils.getStringValue(arg2, env));
        String fundType = FunctionUtils.getStringValue(arg3, env);
        //查询税率
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(businessCode, fundType);
        return calculate(amount, taxRate, businessCode, fundType, null, null);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2,
                              AviatorObject arg3, AviatorObject arg4, AviatorObject arg5) {
        Number amount = FunctionUtils.getNumberValue(arg1, env);
        String businessCode = normalizeBusinessCode(FunctionUtils.getStringValue(arg2, env));
        String fundType = FunctionUtils.getStringValue(arg3, env);
        String leaseType = normalizeLeaseType(FunctionUtils.getStringValue(arg4, env));
        String leaseMethod = normalizeLeaseMethod(FunctionUtils.getStringValue(arg5, env));
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(
                businessCode, fundType, leaseType, leaseMethod);
        return calculate(amount, taxRate, businessCode, fundType, leaseType, leaseMethod);
    }

    private AviatorObject calculate(Number amount, BigDecimal taxRate, String businessCode, String fundType,
                                    String leaseType, String leaseMethod) {
        if (taxRate == null) {
            throw new ServiceException("未配置有效税率，业务类型=" + businessCode
                    + "，金额类型=" + fundType + "，租赁类型=" + leaseType + "，租赁方式=" + leaseMethod);
        }
        BigDecimal amountDecimal = BigDecimal.valueOf(amount.doubleValue());
        BigDecimal result = NumberUtil.div(amountDecimal, NumberUtil.add(1, taxRate))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return new AviatorDecimal(result);
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
        return "noTaxAmount";
    }
}
