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
public enum ModuleEnum {
    //report:报表模块
    REPORT("report", "报表模块"),

    SPECIAL_CONTRACT("special_contract", "特殊合同状态模块"),

    QUERY_MODULE("query_module", "查询功能"),

    PAYABLE_INSURANCE("payable_insurance", "应付保险费模块"),

    MARGIN_CONTRACT("margin_contract", "保证金模块"),

    CHARGE_OFF_SUMMARY("charge_off_summary", "chargOff汇总模块"),

    VERIFICATION_SUMMARY("verification_summary", "核销回款汇总模块"),

    ;

    private final String code;
    private final String desc;

    ModuleEnum(String code, String desc){
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
        for (ModuleEnum enums : ModuleEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static ModuleEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ModuleEnum enums : ModuleEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
