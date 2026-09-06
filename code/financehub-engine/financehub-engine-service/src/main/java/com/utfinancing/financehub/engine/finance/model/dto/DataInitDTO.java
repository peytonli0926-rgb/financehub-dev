package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataInitDTO implements Serializable {

    private String transferSystemCode;

    private String systemCode;

    /**
     * 初始化月份
     */
    private String initDate;
}
