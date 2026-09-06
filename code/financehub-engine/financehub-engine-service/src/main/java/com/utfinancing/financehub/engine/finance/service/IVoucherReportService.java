package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;

import java.util.List;

public interface IVoucherReportService {
    /**
     * 应付款项凭证报表查询-第一层
     */
    public R<IPage<PayablesReportQueryOutputDTO>> payablesReportQuery(PayablesReportQueryInputDTO input);

    /**
     * 付款详情查询-第二层
     */
    R<List<PaymentDetailQueryOutputDTO>> paymentDetailQuery(PaymentDetailQueryInputDTO input);

    /**
     * 合同详情查询-第三层
     */
    public R<List<ContractDetailQueryOutputDTO>> contractDetailQuery(ContractDetailQueryInputDTO input);

    /**
     * 凭证详情查询-第四层
     */
    public R<List<VoucherDetailQueryDTOOutput>> voucherDetailQuery(VoucherDetailQueryDTOInput input);
}
