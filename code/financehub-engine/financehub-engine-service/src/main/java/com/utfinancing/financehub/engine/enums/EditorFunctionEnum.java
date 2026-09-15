package com.utfinancing.financehub.engine.enums;

/**
 * @Author : lixin
 * @Date : Create in 14/09/2023
 */
public enum EditorFunctionEnum {

    scale2("scale2", "返回两位小数","返回两位小数：返回四舍五入后包含两位小数的数字 <br/> 用法： scale2({数字})"),
    taxRate("taxRate", "查询适用税率", "按业务类型、金额类型、租赁类型和租赁方式读取税率配置。<br/>用法：taxRate('ZLYW','lease_interest_receivable','FINANCE_LEASE','DIRECT_LEASE')"),
    noTaxAmount("noTaxAmount", "计算不含税金额", "按税率配置反算不含税金额。<br/>用法：noTaxAmount({含税金额},'ZLYW','lease_interest_receivable','FINANCE_LEASE','DIRECT_LEASE')"),

    ;
    private final String code;

    private final String name;

    private final String desc;

    EditorFunctionEnum(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
