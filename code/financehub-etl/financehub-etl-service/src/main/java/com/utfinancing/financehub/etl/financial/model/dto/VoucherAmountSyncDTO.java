package com.utfinancing.financehub.etl.financial.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class VoucherAmountSyncDTO implements Serializable {
    private List<String> contractCodeList = new ArrayList<>();
}
