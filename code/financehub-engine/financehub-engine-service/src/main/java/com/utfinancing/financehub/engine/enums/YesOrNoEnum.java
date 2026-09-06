package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据类型
 */
public enum YesOrNoEnum {
    YES("1", "是"),
    NO("0", "否"),

    ;
    private final String code;
    private final String desc;

    YesOrNoEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (YesOrNoEnum enums : YesOrNoEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static String getCodeByDesc(final String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        for (YesOrNoEnum enums : YesOrNoEnum.values()) {
            if (desc.equals(enums.desc)) {
                return enums.code;
            }
        }
        return null;
    }

    public static String getDescByBool(final Boolean boo) {
        if (boo == null) {
            return null;
        }
        return boo ? YES.getDesc() : NO.getDesc();
    }
    public static String getCodeByBool(final Boolean boo) {
        if (boo == null) {
            return null;
        }
        return boo ? YES.getCode() : NO.getCode();
    }
}
