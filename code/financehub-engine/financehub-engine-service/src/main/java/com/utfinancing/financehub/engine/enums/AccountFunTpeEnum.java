package com.utfinancing.financehub.engine.enums;

public enum AccountFunTpeEnum {
    DEPRECIATION_RESERVES("6701.04.03", "depreciation_reserves"),
    OUTTAX_AMOUNT("2221.01.05", "outtax_amount"),
    TAX_AMOUNT("2221.01.05", "tax_amount"),
    LEASE_REVENUE_AMOUNT("6041.01,6041.03,6041.04", "lease_revenue_amount"),
    ;
    private final String code;
    private final String desc;

    AccountFunTpeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
