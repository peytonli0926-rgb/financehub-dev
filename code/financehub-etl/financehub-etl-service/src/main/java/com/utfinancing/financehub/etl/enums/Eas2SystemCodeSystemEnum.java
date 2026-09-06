package com.utfinancing.financehub.etl.enums;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum</li>
 * <li>CreateTime : 2024/04/07 20:17</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum Eas2SystemCodeSystemEnum {
    EAS1("EAS1", "eas1"),
    KINGDEE_MIDDLE("KINGDEE_MIDDLE", "金蝶中间库"),
    FINHUB("FINHUB","中台"),

    FINHUB_ENTRY("FINHUB_ENTRY","中台分录");
    ;
    private final String code;
    private final String desc;

    Eas2SystemCodeSystemEnum(String code, String desc) {
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
