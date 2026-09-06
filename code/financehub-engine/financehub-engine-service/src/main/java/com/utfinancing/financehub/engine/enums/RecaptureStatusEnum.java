package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 保证金状态类型
 */
public enum RecaptureStatusEnum {
    NOTRECOVERED("1", "未回笼", "未还"),
    RETURNED("2", "已回笼", "已还"),
    PARTIALRECOVERY("3", "部分回笼", "部分还"),

    ;
    private final String code;
    private final String desc;
    private final String descHY;

    RecaptureStatusEnum(String code, String desc, String descHY) {
        this.code = code;
        this.desc = desc;
        this.descHY = descHY;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 判断是否逾期时，未回笼和部分回笼的偿还计划属于逾期
     *
     * @return
     */
    public static List<String> overDueStatus() {
        List<String> result = new ArrayList<>();
        result.add(NOTRECOVERED.getCode());
        result.add(PARTIALRECOVERY.getCode());
        return result;
    }

    public static String getCodeByDesc(String desc) {
        for (RecaptureStatusEnum myEnum : RecaptureStatusEnum.values()) {
            if (myEnum.desc.equals(desc)) {
                return myEnum.code;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        for (RecaptureStatusEnum myEnum : RecaptureStatusEnum.values()) {
            if (myEnum.code.equals(code)) {
                return myEnum.desc;
            }
        }
        return null;
    }

    public static String getCodeByDescHY(String desc) {
        for (RecaptureStatusEnum myEnum : RecaptureStatusEnum.values()) {
            if (myEnum.descHY.equals(desc)) {
                return myEnum.code;
            }
        }
        return null;
    }


}
