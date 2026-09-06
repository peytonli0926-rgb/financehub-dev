package com.utfinancing.financehub.engine.enums;

/**
 * 系统类型
 */
public enum MqErrorMessageStatusEnum {
    NOT_PROCESS("0", "未处理"),
    REPUSH("1", "已重推"),
    IGNORE("2", "已忽略"),
    ;
    private final String code;
    private final String desc;

    MqErrorMessageStatusEnum(String code, String desc) {
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
