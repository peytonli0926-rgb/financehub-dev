package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeneratePaymentDataProcessDTO implements Serializable {

    private String transferSystemCode;

    private String systemCode;

    private int dataStartIndex;

    private int dataCount;

    private String initDate;
}
