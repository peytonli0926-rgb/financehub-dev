package com.utfinancing.financehub.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccrualTypeEnum {

    ADDITIONAL("0", "新增"),
    ADJUST("1", "调整"),
    ;
    private final String code;
    private final String desc;


    public static String getDescByCode(String code) {
        for (AccrualTypeEnum myEnum : AccrualTypeEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }

    public static String getCodeByDesc(String code) {
        for (AccrualTypeEnum myEnum : AccrualTypeEnum.values()) {
            if (myEnum.desc.equals(code)) {
                return myEnum.code;
            }
        }
        return null;
    }

}
