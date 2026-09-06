package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CheckTypeEnum</li>
 * <li>CreateTime : 2023/10/12 09:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
//对账差异
public enum CheckDiffEnum {
    //JOB:定时任务触发,MANUAL:界面手工触发
    NO_DIFF("0", "无差异"),
    DIFF("1", "有差异")

    ;

    private final String code;
    private final String desc;

    CheckDiffEnum(String code, String desc){
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
        for (CheckDiffEnum enums : CheckDiffEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static CheckDiffEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckDiffEnum enums : CheckDiffEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
