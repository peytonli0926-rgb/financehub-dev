package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectNoRecaptureByContractDTO implements Serializable {
    private String contractCode;
    private String planDate;
    private String recaptureStatus;
}
