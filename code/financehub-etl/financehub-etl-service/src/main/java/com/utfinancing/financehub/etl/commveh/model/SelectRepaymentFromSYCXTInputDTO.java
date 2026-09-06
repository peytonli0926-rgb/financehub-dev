package com.utfinancing.financehub.etl.commveh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SelectRepaymentFromSYCXTInputDTO implements Serializable {
    // 合同编码
    private List<String> contractCodeList;
}
