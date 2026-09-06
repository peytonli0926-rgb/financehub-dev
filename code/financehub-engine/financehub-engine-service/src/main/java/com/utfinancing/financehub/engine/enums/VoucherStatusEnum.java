package com.utfinancing.financehub.engine.enums;

/**
 * 凭证状态 创建 -> 提交 -> 复核 -> 同步(至金蝶)
 */
public enum VoucherStatusEnum {
    CREATE("0", "创建"),
    SUBMIT("10", "提交"),
    RECHECK("20", "复核"),
    FINISHED("90", "同步(至金蝶)"),
    ;
    private final String code;
    private final String desc;

    VoucherStatusEnum(String code, String desc) {
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
