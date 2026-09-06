package com.utfinancing.financehub.etl.commveh.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectNonConfirmAmountInputDTO implements Serializable {
    private String queryDate;
    private String snapshootDate;
}
