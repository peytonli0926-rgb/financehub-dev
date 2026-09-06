package com.utfinancing.financehub.engine.enums;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.ClaimOrderSpecialExpenseTypeEnum</li>
 * <li>CreateTime : 2023/11/23 17:10</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum ClaimOrderSpecialExpenseTypeEnum {
    INSTALLATION_COST ("1","安装费"),
    SERVICE_COST ("2","服务费"),
    ;
    private final String code;
    private final String desc;

    ClaimOrderSpecialExpenseTypeEnum(String code, String desc) {
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
