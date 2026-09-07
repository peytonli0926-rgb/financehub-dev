package com.utfinancing.financehub.engine.payment.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackProfitSharingDTO;
import com.utfinancing.financehub.engine.payment.service.RetailLeasebackProfitSharingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "零售融资租赁回租分润费确认接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/retail-leaseback/profit-sharing")
public class RetailLeasebackProfitSharingController {

    private final RetailLeasebackProfitSharingService service;

    @PostMapping("/execute")
    @ApiOperation("接收分润费确认或退回事件并生成会计凭证")
    public R<List<VoucherDTO>> execute(@Valid @RequestBody RetailLeasebackProfitSharingDTO dto) {
        return R.ok(service.execute(dto));
    }
}
