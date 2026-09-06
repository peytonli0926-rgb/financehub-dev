package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDetailQueryInputDTO implements Serializable {
    private String periodCode;
    private String orgId;
}
