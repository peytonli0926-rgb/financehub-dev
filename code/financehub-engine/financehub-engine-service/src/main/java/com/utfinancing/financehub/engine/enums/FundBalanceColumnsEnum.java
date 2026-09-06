package com.utfinancing.financehub.engine.enums;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.FundBalanceColumnsEnum</li>
 * <li>CreateTime : 2023/12/05 16:00</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum FundBalanceColumnsEnum {
    BANK_NO ("bank_no",   "银行账号"),
    VOUCHER_ID ("voucher_id","凭证ID"),
    ORDER_ID ("order_id","交易流水号"),
    VOUCHER_DATE("voucher_date","记账日期"),
    TRANSACTION_TYPE("transaction_type","交易类型"),
    ;
    private final String code;
    private final String desc;

    FundBalanceColumnsEnum(String code, String desc) {
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
