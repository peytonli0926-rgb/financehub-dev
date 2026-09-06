package com.utfinancing.financehub.engine.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/11 17:30
 */
@ToString
@Data
public class HthxFundEbankTransactionDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @description: 交易类型(必填,收款：collection,付款：payment)
     **/
    private String transactionType;

    /**
     * @description: 收款类型
     **/
    private String collectionType;

    /**
     * @description: 交易日期(yyyy-MM-dd HH:mm:ss)
     **/
    private String businessDate;

    /**
     * @description: 操作日期(yyyy-MM-dd HH:mm:ss)
     **/
    private String operationDate;

    /**
     * @description: 业务事件
     **/
    private String businessOperation;

    /**
     * @description: 网银编号
     **/
    private String ebankNumber;

    /**
     * @description: 收款账号（虚拟户）
     **/
    private String collectionAccountsBankNo;

    /**
     * @description: 收款开户行
     **/
    private String collectionAccountsBank;

    /**
     * @description: 对方合同号
     **/
    private String contractCode;

    /**
     * @description: 客户编号
     **/
    private String clientCode;

    /**
     * @description: 客户名称
     **/
    private String clientName;

    /**
     * @description: 网银金额
     **/
    private BigDecimal bankAmount;

    /**
     * @description: 银行交易摘要
     **/
    private String bankSummary;

    /**
     * @description: 备注
     **/
    private String comment;

    /**
     * @description: 对方客户开户行
     **/
    private String clientAccountsBank;

    /**
     * @description: 对方客户银行账号
     **/
    private String clientAccountsBankNo;

    /**
     * @description: 币种
     **/
    private String currencyType;

}

