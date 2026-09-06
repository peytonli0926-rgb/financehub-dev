package com.utfinancing.financehub.engine.enums;

import org.apache.logging.log4j.util.Strings;

/**
 * 系统类型
 */
public enum BusinessEnum {
    ZLYW("ZLYW", "租赁业务"),
    ZJXT("ZJXT", "资金系统"),
    BLYW("BLYW", "保理业务"),
    WDYW("WDYW", "委贷业务"),
    DEFAULT("default", "默认"),
    ;
    private final String code;
    private final String desc;

    BusinessEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (BusinessEnum anEnum : BusinessEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum.getDesc();
            }
        }
        return code;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
