package com.utfinancing.financehub.etl.passveh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectSurplusDepositOutputDTO implements Serializable {
    private String ebankSerialNumber;
    private String ebankNumber;
    private String collectionAccountsBank;
    private BigDecimal incomeAmount;
    private BigDecimal nonOutcomeAmount;
    private String contractCode;
    private String clientName;
    private String contractStatus;
}
