package com.utfinancing.financehub.engine.enums;

/**
 * 税率的金额类型
 */
public enum TaxFundTypeEnum {
    TAX_GENERAL("tax_general", "通用"),
    RECEIVABLE_COMMISSION("receivable_commission", "手续费"),
    RECEIVABLE_INSURANCE("receivable_insurance", "保险费"),

    ;
    private final String code;
    private final String desc;

    TaxFundTypeEnum(String code, String desc) {
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
