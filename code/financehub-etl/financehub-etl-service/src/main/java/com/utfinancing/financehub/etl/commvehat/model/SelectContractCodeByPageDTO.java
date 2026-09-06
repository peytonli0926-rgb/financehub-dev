package com.utfinancing.financehub.etl.commvehat.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectContractCodeByPageDTO implements Serializable {
    private int end;
    private int start;
}
