package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContractDetailQueryInputDTO implements Serializable {
    private String paymentIdentifier;
}
