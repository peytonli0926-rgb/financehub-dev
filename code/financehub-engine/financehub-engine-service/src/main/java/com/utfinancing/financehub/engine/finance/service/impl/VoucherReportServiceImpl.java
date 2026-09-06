package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.mapper.VoucherMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.service.IVoucherReportService;
import com.utfinancing.financehub.engine.rule.mapper.InterfaceDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class VoucherReportServiceImpl implements IVoucherReportService {

    @Resource
    private VoucherMapper voucherMapper;

    @Resource
    private InterfaceDataMapper interfaceDataMapper;

    /**
     * 应付款项凭证报表查询-第一层
     */
    public R<IPage<PayablesReportQueryOutputDTO>> payablesReportQuery(PayablesReportQueryInputDTO input) {
        input.setLimit(input.getPageSize());
        input.setOffset(input.getPageSize()*(input.getPageNum() - 1));
        List<PayablesReportQueryOutputDTO> dataList = voucherMapper.payablesReportQuery(input);
        IPage<PayablesReportQueryOutputDTO> page = new Page<>(input.getPageNum(), input.getPageSize());
        page.setRecords(dataList);
        page.setTotal(voucherMapper.payablesReportQueryCount(input));
        return R.ok(page);
    }

    /**
     * 付款详情查询-第二层
     */
    public R<List<PaymentDetailQueryOutputDTO>> paymentDetailQuery(PaymentDetailQueryInputDTO input) {
        List<PaymentDetailQueryOutputDTO> dataList = voucherMapper.paymentDetailQuery(input);
        return R.ok(dataList);
    }

    /**
     * 合同详情查询-第三层
     */
    public R<List<ContractDetailQueryOutputDTO>> contractDetailQuery(ContractDetailQueryInputDTO input) {
        List<ContractDetailQueryOutputDTO> dataList = interfaceDataMapper.contractDetailQuery(input);
        return R.ok(dataList);
    }

    /**
     * 凭证详情查询-第四层
     */
    public R<List<VoucherDetailQueryDTOOutput>> voucherDetailQuery(VoucherDetailQueryDTOInput input) {
        List<VoucherDetailQueryDTOOutput> result = voucherMapper.voucherDetailQuery(input);
        return R.ok(result);
    }
}
