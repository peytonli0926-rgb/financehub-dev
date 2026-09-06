package com.utfinancing.financehub.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FundPaymentTypeEnum {

    PAYMENT_TYPE_202("202", "保险费支出"),
    PAYMENT_TYPE_213("213", "渠道费支出");

    private final String code;
    private final String description;

}
