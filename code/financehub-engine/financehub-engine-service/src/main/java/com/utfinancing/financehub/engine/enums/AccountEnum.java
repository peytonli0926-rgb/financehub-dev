package com.utfinancing.financehub.engine.enums;

/**
 * 系统类型
 */
public enum AccountEnum {
    YFBXFZG("2701.03.01", "应付保险费-暂估"),

    ;
    private final String code;
    private final String desc;

    AccountEnum(String code, String desc) {
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
