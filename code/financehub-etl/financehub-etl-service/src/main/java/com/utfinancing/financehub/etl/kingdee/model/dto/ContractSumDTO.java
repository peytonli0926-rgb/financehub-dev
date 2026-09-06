package com.utfinancing.financehub.etl.kingdee.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 13/11/2023
 */
@Data
public class ContractSumDTO {


    private String contractCode;
    private String clientCode;
    private String accountCode;
    private String accountName;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;


}
