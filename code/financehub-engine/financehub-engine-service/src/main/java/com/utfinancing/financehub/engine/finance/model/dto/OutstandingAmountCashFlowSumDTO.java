package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OutstandingAmountCashFlowSumDTO implements Serializable {

    private String contractCode;

    private BigDecimal cashFlowSum;

    private BigDecimal cashflowInit;

    private BigDecimal oustandingAmount;

    private BigDecimal residualValueAmount;
}
