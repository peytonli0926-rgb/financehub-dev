package com.utfinancing.financehub.engine.constants;

/**
 * 正则表达式常量
 */
public class RegexExpConst {


    /**
     * 匹配网银编号
     */
    public static String REGEX_ONLINE_BANKING_NUMBER = "2\\d+(?=[-,])";
    public static String REGEX_OLD_ONLINE_BANKING_NUMBER = "C\\d{8}-\\d{4}";

}
