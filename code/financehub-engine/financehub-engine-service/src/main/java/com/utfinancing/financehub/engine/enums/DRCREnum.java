package com.utfinancing.financehub.engine.enums;

/**
 * 借贷方向
 */
public enum DRCREnum {
    DR("DR", "借方"),
    CR("CR", "贷方"),
    ;
    private final String code;
    private final String desc;

    DRCREnum(String code, String desc) {
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
