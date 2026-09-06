package com.utfinancing.financehub.etl.enums;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.enums.DataExecutionTaskStatusEnum</li>
 * <li>CreateTime : 2024/04/07 10:12</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
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
