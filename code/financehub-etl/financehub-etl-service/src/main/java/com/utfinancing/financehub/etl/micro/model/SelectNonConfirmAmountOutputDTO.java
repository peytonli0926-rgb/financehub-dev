package com.utfinancing.financehub.etl.micro.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectNonConfirmAmountOutputDTO implements Serializable {
    private String isTransferTemporaryCredit;
    private String ebankSerialNumber;
    private String collectionAccountsBankName;
    private String paymentClientName;
    private Date businessDate;
    private BigDecimal ebankAmount;
    private BigDecimal confirmedAmount;
    private BigDecimal nonConfirmedAmount;
    private String remark;
    private String collectionAccountsBankOrgName;
}
