package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;

import java.util.List;

public interface VoucherTransactionService {

    /**
     * 将EAS1的凭证保存到EAS2
     */
    public R<Boolean> transferVoucherToEAS(QueryEas1VoucherInputDTO params) throws Exception;

    void middleVoucherToEas2(List<EasVoucherDTO> sourceVoucherList,String SystemCode) throws Exception;
}
