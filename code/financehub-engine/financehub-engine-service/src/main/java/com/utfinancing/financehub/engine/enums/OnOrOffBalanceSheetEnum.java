package com.utfinancing.financehub.engine.enums;

/**
 * 数据类型
 */
public enum OnOrOffBalanceSheetEnum {
    OFF("1", "表外"),
    ON("0", "表内"),

    ;
    private final String code;
    private final String desc;

    OnOrOffBalanceSheetEnum(String code, String desc) {
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
