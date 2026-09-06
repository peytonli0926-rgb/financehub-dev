package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateXirrPaymentDataProcessDTO implements Serializable {
    // 合同列表
    private List<String> contractCodeList = new ArrayList<>();

    // 系统编码
    private String systemCode;
}
