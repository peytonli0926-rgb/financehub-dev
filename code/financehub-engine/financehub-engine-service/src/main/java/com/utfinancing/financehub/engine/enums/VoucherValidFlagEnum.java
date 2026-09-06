package com.utfinancing.financehub.engine.enums;

/**
 * 凭证有效标识
 */
public enum VoucherValidFlagEnum {
    VALID("1", "有效"),
    ENTRY_EMPTY("2", "凭证行为空"),
    NOT_EQUALS("3", "借贷金额不平"),

    NO_VALID("4", "无效"),

    STAGE("5", "暂存"),

    ;
    private final String code;
    private final String desc;

    VoucherValidFlagEnum(String code, String desc) {
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
