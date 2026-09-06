package com.utfinancing.financehub.engine.enums;

/**
 * 业务系统交易数据任务状态
 */
public enum DataExecutionTaskStatusEnum {
    NOT_START("0", "未开始"),
    RUNNING("1", "进行中"),
    SUCCESS("2", "成功"),
    FAILED("3", "失败"),
    ;
    private final String code;
    private final String desc;

    DataExecutionTaskStatusEnum(String code, String desc) {
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
