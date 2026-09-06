package com.utfinancing.financehub.common.core.enums;

/**
 * <ul>
 * <li>Project : financehub-common</li>
 * <li>ClassName : com.utfinancing.financehub.common.core.enums.SystemRoleEnums</li>
 * <li>CreateTime : 2024/01/09 11:00</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */

public enum SystemRoleEnums {

    ENTER_CODE("1001","录入权限"),

    QUERY_CODE("1002", "查询权限"),

    REVIEW_CODE("1003", "复核权限"),
    ;

    SystemRoleEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
