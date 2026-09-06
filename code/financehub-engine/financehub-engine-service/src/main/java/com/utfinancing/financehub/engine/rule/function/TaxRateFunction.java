package com.utfinancing.financehub.engine.rule.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
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
        String businessCode = FunctionUtils.getStringValue(arg1, env);
        String fundType = FunctionUtils.getStringValue(arg2, env);
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(businessCode, fundType);
        return new AviatorDecimal(taxRate);
    }

    @Override
    public String getName() {
        return "taxRate";
    }
}
