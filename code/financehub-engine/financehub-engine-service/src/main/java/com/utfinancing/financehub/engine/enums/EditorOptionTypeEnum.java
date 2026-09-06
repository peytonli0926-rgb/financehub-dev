package com.utfinancing.financehub.engine.enums;

/**
 * 表达式编辑器选项类型
 */
public enum EditorOptionTypeEnum {
    FIELD("field", "接口表字段"),
    FUN("fun", "函数"),

    ;
    private final String code;
    private final String desc;

    EditorOptionTypeEnum(String code, String desc) {
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
