package com.utfinancing.financehub.engine.enums;

/**
 * 凭证生成方式
 */
public enum VoucherWayEnum {
    AUTO("AUTO", "自动生成"),
    MANUAL("MANUAL", "手动凭证"),
    ;
    private final String code;
    private final String desc;

    VoucherWayEnum(String code, String desc) {
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
