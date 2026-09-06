package com.utfinancing.financehub.engine.enums;

/**
 * 关账期间状态
 */
public enum CloseAccountStatusEnum {
    INVALID("无效", "无效"),
    VALID("有效", "有效"),

    ;
    private final String code;
    private final String desc;

    CloseAccountStatusEnum(String code, String desc) {
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
