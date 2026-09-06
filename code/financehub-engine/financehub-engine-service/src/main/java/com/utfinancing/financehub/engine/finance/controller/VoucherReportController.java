package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.service.IVoucherReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "凭证报表")
@Slf4j
@RestController
@RequestMapping("/finance/voucherReport")
public class VoucherReportController {
    @Resource
    private IVoucherReportService voucherReportService;


    @PostMapping("/query")
    @ApiOperation(value = "应付账款报表查询-第一层")
    public R<IPage<PayablesReportQueryOutputDTO>> payablesReportQuery(@Valid @RequestBody PayablesReportQueryInputDTO dto) {
        return voucherReportService.payablesReportQuery(dto);
    }

    @PostMapping("/paymentDetailQuery")
    @ApiOperation(value = "查看付款详情-第二层")
    public R<List<PaymentDetailQueryOutputDTO>> paymentDetailQuery(@Valid @RequestBody PaymentDetailQueryInputDTO dto) {
        return voucherReportService.paymentDetailQuery(dto);
    }

    @PostMapping("/contractDetailQuery")
    @ApiOperation(value = "合同详情-第三层")
    public R<List<ContractDetailQueryOutputDTO>> contractDetailQuery(@Valid @RequestBody ContractDetailQueryInputDTO dto) {
        return voucherReportService.contractDetailQuery(dto);
    }

    @PostMapping("/voucherDetailQuery")
    @ApiOperation(value = "凭证详情-第四层")
    public R<List<VoucherDetailQueryDTOOutput>> voucherDetailQuery(@Valid @RequestBody VoucherDetailQueryDTOInput dto) {
        return voucherReportService.voucherDetailQuery(dto);
    }
}
