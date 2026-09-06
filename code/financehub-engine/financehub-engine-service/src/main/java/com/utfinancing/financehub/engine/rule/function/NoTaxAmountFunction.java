package com.utfinancing.financehub.engine.rule.function;

import cn.hutool.core.util.NumberUtil;
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
        String businessCode = FunctionUtils.getStringValue(arg2, env);
        String fundType = FunctionUtils.getStringValue(arg3, env);
        //查询税率
        BigDecimal taxRate = taxRateService.getValidTaxRateByCode(businessCode, fundType);
        BigDecimal amountDecimal = BigDecimal.valueOf(amount.doubleValue());
        BigDecimal result = NumberUtil.div(amountDecimal, NumberUtil.add(1, taxRate)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return new AviatorDecimal(result);
    }

    @Override
    public String getName() {
        return "noTaxAmount";
    }
}
