package com.utfinancing.financehub.etl.micro.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectRepaymentFromXWXTInputDTO implements Serializable {
    // 合同编码
    private String contractCode;
}
