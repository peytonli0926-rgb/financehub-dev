package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SelectFundEbankTransactionDataByConDTO implements Serializable {
    // 资金系统网银编号
    private List<String> ebankNumberList = new ArrayList<>();
}
