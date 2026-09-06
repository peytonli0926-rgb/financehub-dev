package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectTaAmountOutputDTO implements Serializable {
    private String contractCode;

    private String orgId;

    private BigDecimal sumTaAmount;

    private String busKey;
}
