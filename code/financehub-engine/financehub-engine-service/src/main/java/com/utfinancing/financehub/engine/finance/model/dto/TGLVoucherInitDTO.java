package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TGLVoucherInitDTO {
    private String fid;

    private String orgId;

    private String period;
    private String contractCode;

    private String clientCode;


    private String accountNumber;

    private String accountName;

    private String fabStract;

    private BigDecimal dtAmount;

    private BigDecimal crAmount;
}
