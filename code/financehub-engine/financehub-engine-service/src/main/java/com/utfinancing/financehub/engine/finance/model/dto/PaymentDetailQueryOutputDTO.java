package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDetailQueryOutputDTO implements Serializable {
    private String orgId;
    private String orgName;
    private Date businessDate;
    private String clientCode;
    private String paymentIdentifier;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal diffAmount;
}
