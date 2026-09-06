package com.utfinancing.financehub.etl.kingdee.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 16/11/2023
 */
@Data
public class KingdeeContractBalanceDTO {

    private String orgId;
    private String contractCode;
    private String clientCode;
    private String accountCode;
    private BigDecimal accountBalance;


}
