package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 保证金状态类型
 */
public enum MarginStatusEnum {
    NOT_ENTERED("0", "未录入"),
    ENTERED("1", "已录入"),
    SUBMITTED("2", "已提交"),
    WRITE_OFF("6", "已冲销"),
    PASS("3", "复核通过"),
    FAILED("5", "复核失败"),
    TO_KINGDEE("4", "已传至金蝶"),

    ;
    private final String code;
    private final String desc;
    MarginStatusEnum(String code, String desc) {
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
        for (MarginStatusEnum myEnum : MarginStatusEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }

    public static List<String> cantChangeStatus(){
        List<String> result = new ArrayList<>();
        result.add(SUBMITTED.getCode());
        result.add(PASS.getCode());
        result.add(TO_KINGDEE.getCode());
        return result;
    }

    public static List<String> canChangeStatus(){
        List<String> result = new ArrayList<>();
        result.add(NOT_ENTERED.getCode());
        result.add(ENTERED.getCode());
        result.add(FAILED.getCode());
        return result;
    }
}
