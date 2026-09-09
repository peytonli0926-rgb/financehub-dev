package com.utfinancing.financehub.engine.payment.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.payment.model.dto.RetailLeasebackAdditionalEventDTO;
import com.utfinancing.financehub.engine.payment.service.RetailLeasebackAdditionalEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "零售融资租赁补充业务事件接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/retail-leaseback")
public class RetailLeasebackAdditionalEventController {
    private final RetailLeasebackAdditionalEventService service;

    @PostMapping("/refund/execute") @ApiOperation("退款事件生成凭证")
    public R<List<VoucherDTO>> refund(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeRefund(dto)); }

    @PostMapping("/subsidy-confirm/execute") @ApiOperation("贴息确认事件生成凭证")
    public R<List<VoucherDTO>> subsidy(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeSubsidy(dto)); }

    @PostMapping("/overdue/execute") @ApiOperation("逾期事件生成凭证")
    public R<List<VoucherDTO>> overdue(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeOverdue(dto)); }

    @PostMapping("/transaction-structure-change/execute") @ApiOperation("交易结构变更事件生成凭证")
    public R<List<VoucherDTO>> structure(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeStructure(dto)); }

    @PostMapping("/auxiliary-account-adjustment/execute") @ApiOperation("辅助账调整事件生成凭证")
    public R<List<VoucherDTO>> auxiliary(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeAuxiliary(dto)); }

    @PostMapping("/other/execute") @ApiOperation("其他事件生成凭证")
    public R<List<VoucherDTO>> other(@Valid @RequestBody RetailLeasebackAdditionalEventDTO dto) { return R.ok(service.executeOther(dto)); }
}
