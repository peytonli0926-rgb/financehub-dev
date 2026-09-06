package com.utfinancing.financehub.engine.enums;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.LeaseTypeEnum</li>
 * <li>CreateTime : 2024/03/12 11:25</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum LeaseTypeEnum {

    DIRECT("直租", "直租"),
    LEASEBACK ("回租", "回租"),

    ;
    private final String code;
    private final String desc;

    LeaseTypeEnum(String code, String desc) {
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
