package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.model.dto.GeneratePaymentDataProcessDTO;

import java.text.ParseException;

public interface IRepaymentAsyncSerivice {

    public void generatePaymentDataProcess(GeneratePaymentDataProcessDTO params);
}
