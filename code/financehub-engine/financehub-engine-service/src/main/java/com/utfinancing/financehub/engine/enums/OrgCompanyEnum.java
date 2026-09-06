package com.utfinancing.financehub.engine.enums;

/**
 * 签约主体
 */
public enum OrgCompanyEnum {
    // 境外主体
    ORG_60001("60001", "Haitong UT Leasing Irish Holding Corporation Limited"),
    ORG_50001("50001", "海通恒信租赁（香港）有限公司"),
    ORG_00000("00000", "海通恒信金融集团（香港）"),

    ;
    private final String code;
    private final String desc;

    OrgCompanyEnum(String code, String desc) {
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
