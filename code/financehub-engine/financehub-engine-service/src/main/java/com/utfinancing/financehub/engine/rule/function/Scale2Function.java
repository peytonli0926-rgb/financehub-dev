package com.utfinancing.financehub.engine.rule.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.utfinancing.financehub.engine.enums.EditorFunctionEnum;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 18/09/2023
 * @Description Aviator自定义函数：返回包含两位小数的数字
 */
@Component("scale2")
public class Scale2Function extends AbstractFunction {

    @Resource
    private ITaxRateService taxRateService;

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Number value = FunctionUtils.getNumberValue(arg1, env);
        BigDecimal valueDecimal = BigDecimal.valueOf(value.doubleValue()).setScale(2, RoundingMode.HALF_UP);
        return new AviatorDecimal(valueDecimal);
    }

    @Override
    public String getName() {
        return "scale2";
    }
}
