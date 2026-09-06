package com.utfinancing.financehub.etl.commveh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectReceiveRepaymentDTO implements Serializable {

    private String systemCode;

    private String contractCode;

    private Integer periods;

    private Date planDate;

    //实际归还租金
    private BigDecimal actualRepaymentRentAmount;

    //实际归还本金
    private BigDecimal actualRepaymentPrincipalAmount;

    //实际归还利息
    private BigDecimal actualRepaymentInteresAmount;

    //TA金额
    private BigDecimal ta;

    private String ebankSerialNumber;

    private String settlementWay;
}
