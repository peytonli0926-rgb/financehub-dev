package com.utfinancing.financehub.engine.rule.function;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.spring.SpringContextFunctionLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 21/09/2023
 */

public class FunctionTest {

    public static void main(String[] args) {
        AviatorEvaluator.addFunction(new PrintFunction());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "lixin");
        dataMap.put("b", null);
        System.out.println(AviatorEvaluator.execute("print(name)",dataMap));;
        System.out.println(AviatorEvaluator.execute("true&&true"));
//        System.out.println(AviatorEvaluator.execute("c != nil"));;
//
//        System.out.println(StrUtil.addSuffixIfNot(StrUtil.addPrefixIfNot(StrUtil.toString(AviatorEvaluator.execute("name", dataMap)), "'"), "'"));
    }

}
