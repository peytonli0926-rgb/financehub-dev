package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统类型
 */
public enum ClaimOperationTypeEnum {
    BUSINESS_AUTO_CLAIM("BUSINESS_AUTO_CLAIM", "业务系统自动认领"),
    MANUAL_CLAIM("MANUAL_CLAIM", "认领"),
    MANUAL_CANCEL_AMOUNT("MANUAL_CANCEL_AMOUNT", "冲销"),

    MODIFY_EBANK_NO("MODIFY_EBANK_NO", "修改网银编号"),
    MODIFY_INCOME_DATE("MODIFY_INCOME_DATE", "修改入账日期"),
    HANDS_ADJUST_BALANCE("HANDS_ADJUST_BALANCE", "手工调整余额"),
    HANDS_UPLOAD_BANK("HANDS_UPLOAD_BANK", "上传线下网银"),
    ;
    private final String code;
    private final String desc;

    ClaimOperationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 取得有效数据得code
     */
    public static List<String> getValidCode() {
        List<String> result = new ArrayList<>();
        result.add(MANUAL_CLAIM.getCode());
        result.add(MANUAL_CANCEL_AMOUNT.getCode());
        result.add(BUSINESS_AUTO_CLAIM.getCode());
        return result;
    }
    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ClaimOperationTypeEnum enums : ClaimOperationTypeEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }
}
