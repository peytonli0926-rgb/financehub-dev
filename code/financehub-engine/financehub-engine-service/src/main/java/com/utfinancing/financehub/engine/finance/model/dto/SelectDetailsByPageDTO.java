package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectDetailsByPageDTO implements Serializable {
    private Long leaseIncomeId;

    private int size;

    private int startIndex;
}
