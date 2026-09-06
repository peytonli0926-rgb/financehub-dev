package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectClaimDataByConstractInputDTO implements Serializable {
    private String contractCode;

    private Date businessDate;
}
