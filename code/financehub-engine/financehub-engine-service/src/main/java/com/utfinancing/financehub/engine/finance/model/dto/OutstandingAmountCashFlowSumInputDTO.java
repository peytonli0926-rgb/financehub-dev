package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OutstandingAmountCashFlowSumInputDTO implements Serializable {
    private String initDate;

    private String contractCode;
}
