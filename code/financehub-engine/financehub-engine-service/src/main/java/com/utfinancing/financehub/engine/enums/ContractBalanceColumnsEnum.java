package com.utfinancing.financehub.engine.enums;

/**
 * 合同余额表公共字段枚举
 */
public enum ContractBalanceColumnsEnum {
    BUSINESS_CODE   ("business_code",   "业务编码"),
    VOUCHER_ID      ("voucher_id",      "凭证ID"),
    INTERFACE_DATA_ID  ("interface_data_id",      "接口表记录ID"),
    BUSINESS_DATE   ("business_date",   "业务日期"),
    VOUCHER_DATE    ("voucher_date",    "凭证日期"),
    SCENE_CODE      ("scene_code",      "场景编码"),
    CONTRACT_CODE   ("contract_code",   "合同编码"),
    CLIENT_CODE     ("client_code",     "客户编码"),
    CLIENT_TYPE     ("client_type",     "客户类型"),
    CLIENT_NAME     ("client_name",          "客户名称"),
    ORG_ID          ("org_id",          "机构编码"),
    PERIOD_CODE    ("period_code",    "会计日期"),
    CONTRACT_NAME ("contract_name", "合同名称"),
    LEASE_DATE_START ("lease_date_start", "起租日"),
    LEASE_DATE_END ("lease_date_end", "到期日"),
    BUSINESS_NAME ("business_name", "业务类型名称"),
    CURRENCY_TYPE ("currency_type", "币种"),

    BILL_CONTRACT_CODE("bill_contract_code",          "借款合同编号"),

    SYSTEM_CODE ("system_code", "系统编码"),
    ;
    private final String code;
    private final String desc;

    ContractBalanceColumnsEnum(String code, String desc) {
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
