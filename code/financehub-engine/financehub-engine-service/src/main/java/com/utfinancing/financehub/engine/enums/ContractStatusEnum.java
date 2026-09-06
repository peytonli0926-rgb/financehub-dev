package com.utfinancing.financehub.engine.enums;

/**
 * 合同状态
 */
public enum ContractStatusEnum {
    CWRK("财务入库", "财务入库"),
    CWCK("财务出库", "财务出库"),
    CWCZ("财务处置", "财务处置"),
    CWSH("财务赎回", "财务赎回"),
    HTJS("合同结束","合同结束"),
    ;
    private final String code;
    private final String desc;

    ContractStatusEnum(String code, String desc) {
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
