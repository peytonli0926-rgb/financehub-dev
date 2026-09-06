package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SelectNonConfirmCollectionDataDTO implements Serializable {
    private String ebankNumber;
    private String ebankSerialNumber;
    private BigDecimal bankAmount;
    private Long id;
    private String currencyType;
    private String collectionAccountsBankNo;
    private String matchNumber;
}
