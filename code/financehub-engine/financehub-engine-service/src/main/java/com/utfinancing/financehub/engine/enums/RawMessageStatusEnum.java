package com.utfinancing.financehub.engine.enums;

/**
 * 业务数据执行状态
 */
public enum RawMessageStatusEnum {
    NOT_EXECUTE("NOT_EXECUTE", "未执行"),
    RUNNING("RUNNING", "进行中"),
    SUCCESS("SUCCESS", "成功"),
    FAILED("FAILED", "失败"),

    ;
    private final String code;
    private final String desc;

    RawMessageStatusEnum(String code, String desc) {
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
