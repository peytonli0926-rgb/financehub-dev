package com.utfinancing.financehub.etl.enums;

/**
 * 收益计提方式
 */
public enum AccrualMethodEnum {
    XIRR("1", "XIRR分摊收益"),
    RECEIPT("2", "实收"),
    IRR("3", "IRR分摊收益"),

    ;
    private final String code;
    private final String desc;

    AccrualMethodEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (AccrualMethodEnum myEnum : AccrualMethodEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }

    public static String getCodeByDesc(String code) {
        for (AccrualMethodEnum myEnum : AccrualMethodEnum.values()) {
            if (myEnum.desc.equals(code)) {
                return myEnum.code;
            }
        }
        return null;
    }

}
