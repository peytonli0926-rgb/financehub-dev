package com.utfinancing.financehub.common.core.enums;

/**
 * @Author : lixin
 * @Date : Create in 18/09/2023
 */
public enum EnableFlagEnum {
    ENABLE("1", "启用/可用"),
    DISABLE("0", "禁用/不可用"),
    ;

    EnableFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
