package com.utfinancing.financehub.engine.utils;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinhubAmountUtils {

    public static BigDecimal amountNoTax(BigDecimal amount) {
        return NumberUtil.div(amount, new BigDecimal("1.06"), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal amountTaxIncluded(BigDecimal amount) {
        return NumberUtil.mul(amount, new BigDecimal("1.06")).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal amount1,BigDecimal amount2) {
        return amount1.multiply(amount2).setScale(2, RoundingMode.HALF_UP);
    }
    public static BigDecimal divide(BigDecimal amount1,BigDecimal amount2) {
        return amount1.divide(amount2).setScale(2, RoundingMode.HALF_UP);
    }

}
