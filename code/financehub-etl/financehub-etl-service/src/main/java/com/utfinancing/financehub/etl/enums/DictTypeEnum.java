package com.utfinancing.financehub.etl.enums;

/**
 * 常用数据字典枚举
 */
public enum DictTypeEnum {

    ACCRUAL_ABS_LIST("accrual_abs_list", "收益计提abs赎回列表"),
    ACCRUAL_FINANCIAL_CONTRACT_STATUS_LIST("accrual_financial_contract_status_list", "收益计提特殊合同状态列表"),
    CASH_TYPE("sys_cash_type", "金额类型"),
    CONTRACT_FIELDS("sys_contract_fields", "合同表字段列表"),
    CLIENT_FIELDS("sys_client_fields", "客户表字段列表"),
    MARGIN_SUBJECT("sys_margin_subject", "保证金科目"),
    MARGIN_RECLASSIFIED_SCOPE("sys_margin_subject_reclassified", "保证金重分类科目范围"),
    MARGIN_INTEREST_SCOPE("sys_interest_subject", "保证金利息计提科目范围"),
    LEASE_TYPE("lease_type", "租赁类型"),
    FINANCIAL_CONTRACT_STATUS("financial_contract_status", "财务合同状态"),
    CONTRACT_STATUS("business_contract_status", "业务合同状态"),
    LEASE_SUB_TYPE("lease_sub_type", "租赁细类"),
    SYS_CURRENCY_TYPE("sys_currency_type", "币种"),
    SYS_CLIENT_TYPE("sys_client_type", "客户类型"),
    INVOICE_TYPE("invoice_type", "发票类型"),
    INVOICE_FLAG("invoice_flag", "开票标识"),
    ACCRUAL_METHOD("accrual_method", "收益计提方式"),
    PAY_METHOD("pay_method", "还款标识"),
    IS_DIFFER_CONTRACT_STATUS("is_differ_contract_status", "是否区分合同状态"),
    IS_CALCULATE_REVENUE("is_calculate_revenue", "是否计提收益"),
    COMPANY("company", "签约主体"),
    SYS_SUB_SCENE_TYPE("sys_sub_scene_type", "细分场景"),
    SYS_AUTO_EXECUTE_SYSTEM("sys_auto_execute_system", "自动跑凭证的业务系统"),
    MANTISSA_ADJUST_ACCOUNT("mantissa_adjust_account", "尾差调整科目"),
    BUSINESS_CATEGORY("business_category", "业务大类"),
    SYS_TAX_RATE_BONDED_ASSETS("tax_rate_bonded_assets", "抵债资产出租税率"),
    SYS_FORM_SOURCE("sys_form_source", "系统来源"),
    SYS_VOUCHER_TYPE("sys_voucher_type", "凭证类型"),
    ;
    private final String code;
    private final String desc;

    DictTypeEnum(String code, String desc) {
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
