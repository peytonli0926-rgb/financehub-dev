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
//对账记录状态
public enum CheckExecuteStatusEnum {
    //In-Progress:对账执行中,Finish:对账成功完成,Error:对账过程发生错误
    INPROGRESS("In-Progress", "对账执行中"),
    FINISH("Finish", "对账成功完成"),

    ERROR("Error", "对账过程发生错误")

    ;

    private final String code;
    private final String desc;

    CheckExecuteStatusEnum(String code, String desc){
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
        for (CheckExecuteStatusEnum enums : CheckExecuteStatusEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static CheckExecuteStatusEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckExecuteStatusEnum enums : CheckExecuteStatusEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
