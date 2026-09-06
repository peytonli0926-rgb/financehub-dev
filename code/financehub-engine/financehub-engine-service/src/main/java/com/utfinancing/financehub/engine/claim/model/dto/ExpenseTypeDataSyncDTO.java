package com.utfinancing.financehub.engine.claim.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExpenseTypeDataSyncDTO implements Serializable {
    // 费用类型
    private String expenseType;
}
