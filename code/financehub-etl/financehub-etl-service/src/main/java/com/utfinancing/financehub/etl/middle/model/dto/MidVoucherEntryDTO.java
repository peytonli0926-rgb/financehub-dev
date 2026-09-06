package com.utfinancing.financehub.etl.middle.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 14/12/2023
 */
@Data
public class MidVoucherEntryDTO {

    private String businessDate;
    private String voucherDate;
    private String voucherNumber;
    private String systemCode;
    private String sceneName;
    private String orgId;
    private String contractCode;
    private String accountCode;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;

}
