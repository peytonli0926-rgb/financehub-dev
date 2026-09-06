package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    XDWL("XDWL", "现代物流"),
    HYB("HYB", "恒运宝"),
    ZJXT("ZJXT","资金系统"),

    GAXT("GAXT", "贵安系统"),
    HY_XDWL("HY-XDWL", "恒运现代物流"),
    YYPT("YYPT", "运营平台"),
    GYLBL("GYLBL", "供应链保理"),
    YWZT("YWZT","业务中台"),
    HYYW("HYYW","恒运法人系统"),
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

    public static String getCodeByDesc(final String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        for (SystemEnum enums : SystemEnum.values()) {
            if (desc.equals(enums.desc)) {
                return enums.code;
            }
        }
        return null;
    }

    /**
     * @description: 将枚举值转换为Map类型
     * @author: zhangli.chen
     **/
    public static Map<String,String> getEnumToMap(){
        return  Arrays.stream(values()).collect(Collectors.toMap(
                SystemEnum::getCode,
                SystemEnum::getDesc));
    }
}
