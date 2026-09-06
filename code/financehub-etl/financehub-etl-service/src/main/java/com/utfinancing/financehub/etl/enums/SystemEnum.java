package com.utfinancing.financehub.etl.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 系统类型
 */
public enum SystemEnum {
    CWZT("FINHUB", "财务中台"),
    TYPT("TYPT", "统一平台"),
    XWXT("XWXT", "小微系统"),
    SYCXT("SYCXT", "商用车系统"),
    CYCXT("CYCXT", "乘用车系统"),
    KPXT("KPXT", "开票系统"),
    MFXT("MFXT", "魔方系统"),
    ZJXT("ZJXT","资金系统"),
    ;
    private final String code;
    private final String desc;

    SystemEnum(String code, String desc) {
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
        for (SystemEnum enums : SystemEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }
}
