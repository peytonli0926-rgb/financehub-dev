package com.utfinancing.financehub.etl.micro.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectRepaymentFromXWXTDTO implements Serializable {
    // 来源系统
    private String systemCode;
    // 签约主体编号
    private String orgId;
    // 签约主体名称
    private String orgName;
    // 合同编号
    private String contractCode;
    // 合同名称
    private String contractName;
    // 客户编号
    private String clientCode;
    // 客户名称
    private String clientName;
    // 期数
    private String periods;
    // 业务日期
    private Date businessDate;
    // 计划还款日
    private Date planDate;

    //归还租金(不含税)
    private BigDecimal rentAmount;

    //归还本金(不含税)
    private BigDecimal principalAmount;

    //归还利息(不含税)
    private BigDecimal interestAmount;
    //回笼状态
    private String recaptureStatus;

    private BigDecimal sourceIrrRate;

    //现金流
    private BigDecimal cashFlow;
}
