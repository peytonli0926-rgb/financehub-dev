package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 保证金状态类型
 */
public enum ContractStatusRecordStatus {
    ENTERED("1", "已录入"),
    SUBMITTED("2", "已提交"),
    PASS("3", "复核通过"),
    FAILED("4", "复核失败"),

    ;
    private final String code;
    private final String desc;
    ContractStatusRecordStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static List<String> cantChangeStatus(){
        List<String> result = new ArrayList<>();
        result.add(SUBMITTED.getCode());
        result.add(PASS.getCode());
        return result;
    }

    public static List<String> canChangeStatus(){
        List<String> result = new ArrayList<>();
        result.add(ENTERED.getCode());
        result.add(FAILED.getCode());
        return result;
    }

}
