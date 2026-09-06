package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.ProcessStatusEnum</li>
 * <li>CreateTime : 2023/10/12 09:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum ProcessStatusEnum {
    //1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
    NOT_ENTERED("0", "未录入"),
    ENTERED("1", "已录入"),
    SUBMITTED("2", "已提交"),
    REVIEWED("3","已复核"),
    TO_KINGDEE("4","已传至金蝶"),
    REJECTED("5","已拒绝"),
    WRITEOFF("6","已冲销"),
    RECALL("7","已撤回"),
    TEMPORARILY("9","暂存"),

    ;

    private final String code;
    private final String desc;

    ProcessStatusEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ProcessStatusEnum enums : ProcessStatusEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    /**
     * 取得有效数据得code
     */
    public static List<String> getValidCode() {
        List<String> result = new ArrayList<>();
        result.add(REVIEWED.getCode());
        result.add(TO_KINGDEE.getCode());
        return result;
    }

    /**
     * 取得不能修改的code
     */
    public static List<String> getCannotModifyCode() {
        List<String> result = new ArrayList<>();
        result.add(SUBMITTED.getCode());
        result.add(REVIEWED.getCode());
        result.add(TO_KINGDEE.getCode());
        return result;
    }

    /**
     * 取得无效数据得code
     */
    public static List<String> getInvalidCode() {
        List<String> result = new ArrayList<>();
        result.add(NOT_ENTERED.getCode());
        result.add(ENTERED.getCode());
        result.add(REJECTED.getCode());
        return result;
    }

    public static ProcessStatusEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ProcessStatusEnum enums : ProcessStatusEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
