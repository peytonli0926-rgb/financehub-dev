package com.utfinancing.financehub.engine.enums;

/**
 * 凭证行维度
 */
public enum AssistFlagEnum {

    CLIENT("0", "客户"),
    CONTRACT("1", "合同"),
    BANK_NO("2", "银行账号"),
    EBANK_NUMBER("3", "网银编号"),
    BILL_CONTRACT("4", "借款合同编号"),
    ACTUAL_CLIENT("5", "实际客户编号"),
    ;

    private final String code;
    private final String desc;

    AssistFlagEnum(String code, String desc) {
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
