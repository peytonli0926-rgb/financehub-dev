package com.utfinancing.financehub.etl.platform.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectTaAmountDTO implements Serializable {
    private String contractCode;
    private String orgId;
    private String orgName;
    private BigDecimal taAmount;
}
