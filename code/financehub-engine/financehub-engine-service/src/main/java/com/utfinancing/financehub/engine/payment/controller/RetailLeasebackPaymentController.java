package com.utfinancing.financehub.engine.payment.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackPaymentDTO;
import com.utfinancing.financehub.engine.payment.service.RetailLeasebackPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "零售融资租赁回租付款接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/retail-leaseback/payment")
public class RetailLeasebackPaymentController {

    private final RetailLeasebackPaymentService paymentService;

    @PostMapping("/execute")
    @ApiOperation("接收付款事件并生成会计凭证")
    public R<List<VoucherDTO>> execute(@Valid @RequestBody RetailLeasebackPaymentDTO dto) {
        return R.ok(paymentService.execute(dto));
    }
}
