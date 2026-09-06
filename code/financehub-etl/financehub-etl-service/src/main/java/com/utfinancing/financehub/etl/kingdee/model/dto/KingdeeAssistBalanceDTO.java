package com.utfinancing.financehub.etl.kingdee.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 04/01/2024
 */
@Data
public class KingdeeAssistBalanceDTO {

    //会计期间
    private Integer periodCode;
    //签约主体
    private String orgId;
    //合同编码
    private String contractCode;
    //客户编码
    private String clientCode;
    //借款合同编码
    private String billContractCode;
    //币种
    private String currencyCode;
    //科目编号
    private String accountCode;
    //科目名称
    private String accountName;
    //期初余额
    private BigDecimal beginBalance;
    //期末余额
    private BigDecimal endBalance;
    //本期借方发生额
    private BigDecimal debitAmount;
    //本期贷方发生额
    private BigDecimal creditAmount;
    //年累计借方发生额
    private BigDecimal yearDebitAmount;
    //年累计贷方发生额
    private BigDecimal yearCreditAmount;


}
