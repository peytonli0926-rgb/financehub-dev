package com.utfinancing.financehub.etl.platform.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectNonConfirmAmountInputDTO implements Serializable {
    private String queryDate;
}
