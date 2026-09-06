package com.utfinancing.financehub.engine.enums;

/**
 * 数据类型
 */
public enum DataTypeEnum {
    STRING("String", "字符串"),
    NUMBER("Number", "数值"),
    LIST("List", "数组"),

    ;
    private final String code;
    private final String desc;

    DataTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
