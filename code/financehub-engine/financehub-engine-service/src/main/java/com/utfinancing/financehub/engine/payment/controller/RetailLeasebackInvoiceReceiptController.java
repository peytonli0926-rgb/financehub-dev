package com.utfinancing.financehub.engine.payment.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackInvoiceReceiptDTO;
import com.utfinancing.financehub.engine.payment.service.RetailLeasebackInvoiceReceiptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "零售融资租赁回租收到发票接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/retail-leaseback/invoice-receipt")
public class RetailLeasebackInvoiceReceiptController {

    private final RetailLeasebackInvoiceReceiptService service;

    @PostMapping("/execute")
    @ApiOperation("登记收到发票并生成会计凭证")
    public R<List<VoucherDTO>> execute(@Valid @RequestBody RetailLeasebackInvoiceReceiptDTO dto) {
        return R.ok(service.execute(dto));
    }
}
