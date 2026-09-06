package com.utfinancing.financehub.engine.enums;

public enum AllocationMethodEnum {

    RENTAL_INCOME("0", "租赁收入分摊"),
    SERVICE_CHARGE("1", "服务费分摊"),
    ;

    private final String code;
    private final String desc;

    AllocationMethodEnum(String code, String desc) {
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
