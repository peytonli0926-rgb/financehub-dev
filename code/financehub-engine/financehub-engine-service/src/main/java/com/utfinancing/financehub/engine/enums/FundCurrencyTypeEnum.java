package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 资金系统-币种映射
 */
public enum FundCurrencyTypeEnum {
    CNY("Currency1", "人民币", "RMB"),
    CHF("Currency10", "瑞士法郎", "CHF"),
    BEF("Currency11", "比利时法郎", "BEF"),
    EUR("Currency12", "欧元", "EUR"),
    AUD("Currency13", "澳元", "AUD"),
    USD("Currency2", "美元", "USD"),
    GBP("Currency3", "英镑", "GBP"),
    JPY("Currency4", "日元", "JPY"),
    FRF("Currency5", "法国法郎", "FRF"),
    DEM("Currency6", "德国马克", "DEM"),
    HKD("Currency7", "港币", "HKD"),
    CAD("Currency8", "加拿大元", "CAD"),
    NLG("Currency9", "荷兰盾", "NLG"),
    CNY1("currency_type1", "人民币", "RMB"),
    USD1("currency_type2", "美元", "USD"),
    HKD1("currency_type3", "港币", "HKD"),
    EUR1("currency_type4", "欧元", "EUR"),
    JPY1("currency_type5", "日元", "JPY"),


    ;

    private final String type;
    private final String code;
    private final String desc;

    FundCurrencyTypeEnum(String type, String desc, String code) {
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

    public static FundCurrencyTypeEnum getEnumByType(final String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        for (FundCurrencyTypeEnum enums : FundCurrencyTypeEnum.values()) {
            if (type.equals(enums.type)) {
                return enums;
            }
        }
        return null;
    }

    public static FundCurrencyTypeEnum getEnumByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (FundCurrencyTypeEnum enums : FundCurrencyTypeEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
