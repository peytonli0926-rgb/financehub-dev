package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectIncomeDateInfoInputDTO implements Serializable {
    private String businessEbankNumber;
    private String ebankSerialNumber;
    private String incomeDateString;
}
