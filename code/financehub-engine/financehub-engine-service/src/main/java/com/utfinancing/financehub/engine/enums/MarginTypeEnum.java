package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 保证金凭证类型
 */
public enum MarginTypeEnum {
    RECLASSIFICATION("1", "重分类"),
    INTEREST_PROVISION("2", "利息计提"),

    ;
    private final String code;
    private final String desc;
    MarginTypeEnum(String code, String desc) {
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
