package com.utfinancing.financehub.etl.micro.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectNonConfirmAmountInputDTO implements Serializable {
    private String queryDate;
}
