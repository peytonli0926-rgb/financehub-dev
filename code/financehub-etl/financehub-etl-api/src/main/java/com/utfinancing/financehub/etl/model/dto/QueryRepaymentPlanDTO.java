package com.utfinancing.financehub.etl.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRepaymentPlanDTO implements Serializable {
    private String contractCode;

    private String incomeProvisionMethod;
}
