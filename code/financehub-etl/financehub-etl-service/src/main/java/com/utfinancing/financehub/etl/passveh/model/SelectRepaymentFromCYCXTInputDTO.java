package com.utfinancing.financehub.etl.passveh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SelectRepaymentFromCYCXTInputDTO implements Serializable {
    // 合同编码
    private List<String> contractCodeList;
}
