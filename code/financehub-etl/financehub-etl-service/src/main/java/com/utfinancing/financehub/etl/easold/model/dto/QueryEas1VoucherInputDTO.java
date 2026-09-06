package com.utfinancing.financehub.etl.easold.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class QueryEas1VoucherInputDTO implements Serializable {
    private int startIndex;
    private int endIndex;

    private String fnumber;
    private String isUseFid;

    private List<String> orgIdList = new ArrayList<>();
}
