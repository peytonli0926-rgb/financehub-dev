package com.utfinancing.financehub.etl.platform.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectRepaymentFromTYPTInputDTO implements Serializable {
    // 合同编码
    private String contractCode;
}
