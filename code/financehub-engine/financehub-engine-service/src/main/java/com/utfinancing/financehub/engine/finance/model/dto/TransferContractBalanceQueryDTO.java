package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TransferContractBalanceQueryDTO {
    /**
     * 签约主体
     */
    private String orgId;
    /**
     * 合同编码集合
     */
    private List<String> contractCodeList;
    /**
     * 场景编码集合
     */
    private List<String> sceneCodeList;


    /**
     * 凭证日期开始
     */
    private LocalDate voucherDateStart;

    /**
     * 凭证日期结束
     */
    private LocalDate voucherDateEnd;
}
