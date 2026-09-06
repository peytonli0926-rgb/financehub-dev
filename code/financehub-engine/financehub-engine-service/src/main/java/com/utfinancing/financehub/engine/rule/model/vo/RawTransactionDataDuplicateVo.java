package com.utfinancing.financehub.engine.rule.model.vo;

import lombok.Data;

@Data
public class RawTransactionDataDuplicateVo {

    private String md5;
    private String duplicateCount;
    private String ids;
}
