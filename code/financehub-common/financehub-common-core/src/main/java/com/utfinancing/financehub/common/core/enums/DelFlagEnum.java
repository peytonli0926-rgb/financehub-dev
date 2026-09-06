package com.utfinancing.financehub.common.core.enums;

public enum DelFlagEnum {
    DELETED("1", "已删除"),
    NOT_DELETED("0", "正常/未删除"),
    ;

    DelFlagEnum(String code, String desc) {
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
