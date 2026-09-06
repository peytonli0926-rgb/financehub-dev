package com.utfinancing.financehub.engine.enums;

public enum ManualSourceFrom {

    KJFP("KJFP", "开票认领"),
    PZ ("PZ", "凭证"),

    SGPZ ("SGPZ", "手工凭证"),

    ;
    private final String code;
    private final String desc;

    ManualSourceFrom(String code, String desc) {
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
