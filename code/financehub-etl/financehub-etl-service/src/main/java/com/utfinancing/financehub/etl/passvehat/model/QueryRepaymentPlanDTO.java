package com.utfinancing.financehub.etl.passvehat.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRepaymentPlanDTO implements Serializable {
    private String contractCode;

    private String incomeProvisionMethod;
}
