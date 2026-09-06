package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum FinancialContractStatusForExceptionEnum {
    //财务入库
    //服务费核销
    //固定资产入库
    //亏损结清
    //亏损结清、服务费核销
    //入库后处置
    //未入库处置
    //债务重组
    //正常核销
    //正常核销、资产处置结束（第三方转让）
    //正常核销、资产处置结束（第三方转让）、服务费核销
    //资产出表（ABS）
    //资产出表（ABS）后赎回
    //资产处置结束（保理出表）
    //资产处置结束（第三方转让）
    //资产处置结束（第三方转让）、服务费核销
    //资产处置结束（资产交易）
    //亏损结清、资产处置结束（第三方转让）
    //亏损结清、服务费核销、资产处置结束（第三方转让）
    F_ENUM_1("财务入库"),
    F_ENUM_2("服务费核销"),
    F_ENUM_3("固定资产入库"),
    F_ENUM_4("亏损结清"),
    F_ENUM_5("亏损结清、服务费核销"),
    F_ENUM_6("入库后处置"),
    F_ENUM_7("未入库处置"),
    F_ENUM_8("债务重组"),
    F_ENUM_9("正常核销"),
    F_ENUM_10("正常核销、资产处置结束（第三方转让）"),
    F_ENUM_11("正常核销、资产处置结束（第三方转让）、服务费核销"),
    F_ENUM_12("资产出表（ABS）"),
    F_ENUM_13("资产出表（ABS）后赎回"),
    F_ENUM_14("资产处置结束（保理出表）"),
    F_ENUM_15("资产处置结束（第三方转让）"),
    F_ENUM_16("资产处置结束（第三方转让）、服务费核销"),
    F_ENUM_17("资产处置结束（资产交易）"),
    F_ENUM_18("亏损结清、资产处置结束（第三方转让）"),
    F_ENUM_19("亏损结清、服务费核销、资产处置结束（第三方转让）"),
    F_ENUM_20("入库后赎回"),
    ;

    private final String code;

    FinancialContractStatusForExceptionEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    // 检查给定字符串是否存在于枚举中
    public static boolean containsValue(String valueToCheck) {
        for (FinancialContractStatusForExceptionEnum constant : FinancialContractStatusForExceptionEnum.values()) {
            if (constant.getCode().equals(valueToCheck)) {
                return true;
            }
        }
        return false;
    }

}
