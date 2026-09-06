package com.utfinancing.financehub.engine.finance.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectContractByPageVO implements Serializable {
    private int pageSize;
    private int offset;
    private String leaseDateStart;
}
