package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

public enum CurrencyTypeEnum {
    CNY("currency_type1", "人民币", "RMB"),
    USD("currency_type2", "美元", "USD"),
    HKD("currency_type3", "港币","HKD"),
    EUR("currency_type4", "欧元","EUR"),
    JPY("currency_type5", "日元","JPY"),

    GBP("currency_type6", "英镑","GBP"),

    ;

    private final String type;
    private final String code;
    private final String desc;

    CurrencyTypeEnum(String type, String desc, String code) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }


    public String getType() {
        return type;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }


    public static CurrencyTypeEnum getEnumByType(final String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        for (CurrencyTypeEnum enums : CurrencyTypeEnum.values()) {
            if (type.equals(enums.type)) {
                return enums;
            }
        }
        return null;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CurrencyTypeEnum enums : CurrencyTypeEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.getDesc();
            }
        }
        return null;
    }
}
