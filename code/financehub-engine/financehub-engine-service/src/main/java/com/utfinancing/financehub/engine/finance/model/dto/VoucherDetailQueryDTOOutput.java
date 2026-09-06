package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class VoucherDetailQueryDTOOutput implements Serializable {
    private String orgId;
    private String orgName;
    private Date businessDate;
    private Date voucherDate;
    private String systemName;
    private String sceneName;
    private String contract_code;
    private String account_code;
    private String account_name;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal diffAmount;
}
