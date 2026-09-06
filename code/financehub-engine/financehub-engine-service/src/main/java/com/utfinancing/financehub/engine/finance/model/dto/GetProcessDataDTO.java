package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetProcessDataDTO implements Serializable {
    private int pageNum;

    private int pageSize;
}
