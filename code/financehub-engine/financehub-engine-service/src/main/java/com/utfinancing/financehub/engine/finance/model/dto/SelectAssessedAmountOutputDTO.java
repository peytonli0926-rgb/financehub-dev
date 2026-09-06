package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectAssessedAmountOutputDTO implements Serializable {
    private String contractCode;

    private BigDecimal assessedAmount;
}
