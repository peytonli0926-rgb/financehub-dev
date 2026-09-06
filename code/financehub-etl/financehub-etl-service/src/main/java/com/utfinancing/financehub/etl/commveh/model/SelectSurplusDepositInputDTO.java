package com.utfinancing.financehub.etl.commveh.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectSurplusDepositInputDTO implements Serializable {
    private String queryDate;
}
