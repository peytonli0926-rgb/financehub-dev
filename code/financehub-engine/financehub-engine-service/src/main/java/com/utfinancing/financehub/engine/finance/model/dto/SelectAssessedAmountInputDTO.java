package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SelectAssessedAmountInputDTO implements Serializable {
    // 合同编码
    private List<String> contractCodeList;
}
