package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayablesReportQueryOutputDTO implements Serializable {
    private String periodCode;
    private String orgId;
    private String orgName;
    private String accountCode;
    private String accountName;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal diffAmount;
    private String isException;
}
