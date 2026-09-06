package com.utfinancing.financehub.engine.finance.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceFeeMonthlyData {

    private String month;
    private BigDecimal amount;
}
