package com.utfinancing.financehub.engine.rule.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数组金额DTO
 * @Author : lixin
 * @Date : Create in 02/11/2023
 */
@Data
public class ListAmountDTO {

    private String orgId; //签约主体
    private String ebankNum; //网银编号
    private BigDecimal ebankAmount; //转账金额
    private String payableMethod; //付款方式
    private String payableBank; //付款开户行
    private String payableAccount; //付款账号
    private String amountType;//金额类型
    private String bankOrgId;//网银到账主体
    private String ebankSerialNumber;//网银编号、网银流水号
    private String ebankBatchNo;//批扣流水号

}
