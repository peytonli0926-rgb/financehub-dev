package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum FinancialContractStatusLeaseIncomeEnum {
    //正常核销,亏损结清,非亏损结清
    ONE("", "已结束"),
    TWO("", "已结清"),
    ;

    private final String code;
    private final String desc;

    FinancialContractStatusLeaseIncomeEnum(String code, String desc){
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
        for (FinancialContractStatusLeaseIncomeEnum enums : FinancialContractStatusLeaseIncomeEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static List<String> getStatus(){
        List<String> result = new ArrayList<>();
        result.add(ONE.desc);
        result.add(TWO.desc);
        return result;
    }

    public static String getCodeByDesc(final String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        for (FinancialContractStatusLeaseIncomeEnum enums : FinancialContractStatusLeaseIncomeEnum.values()) {
            if (desc.equals(enums.desc)) {
                return enums.code;
            }
        }
        return null;
    }
}
