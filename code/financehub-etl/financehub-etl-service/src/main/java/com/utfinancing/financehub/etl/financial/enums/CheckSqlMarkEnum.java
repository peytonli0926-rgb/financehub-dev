package com.utfinancing.financehub.etl.financial.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.CheckTypeEnum</li>
 * <li>CreateTime : 2023/10/12 09:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
//sql标志
public enum CheckSqlMarkEnum {
    //TODO
    COMMERCIAL_VEHICLE("SYC", "商用车"),
    PASSENGER_VEHICLE("CYC", "乘用车"),
    COMMERCIAL_VEHICLE_ASSET_TRANSFER("SYC_AT", "商用车资产转移"),
    PASSENGER_VEHICLE_ASSET_TRANSFER("CYC_AT", "乘用车资产转移"),
    XW("XW", "小微"),
    PLATFORM("PLATFORM", "统一平台")

    ;

    private final String code;
    private final String desc;

    CheckSqlMarkEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckSqlMarkEnum enums : CheckSqlMarkEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static CheckSqlMarkEnum getInstanceByCode(final String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (CheckSqlMarkEnum enums : CheckSqlMarkEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
