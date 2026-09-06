package com.utfinancing.financehub.engine.enums;

/**
 * @Author : lixin
 * @Date : Create in 14/09/2023
 */
public enum EditorFunctionEnum {

    scale2("scale2", "返回两位小数","返回两位小数：返回四舍五入后包含两位小数的数字 <br/> 用法： scale2({数字})"),

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
