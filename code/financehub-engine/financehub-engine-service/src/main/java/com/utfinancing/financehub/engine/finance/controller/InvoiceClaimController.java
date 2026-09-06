package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.engine.finance.model.vo.InvoiceClaimVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.utfinancing.financehub.engine.finance.service.IInvoiceClaimService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-01-05
 * @Description :   InvoiceClaim控制器实现类
 * @Modified :
 */
@Api(tags = "开票")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/invoice-claim")
public class InvoiceClaimController {

    private final IInvoiceClaimService  invoiceClaimService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody InvoiceClaimDTO dto) {
        return R.ok(invoiceClaimService.saveInvoiceClaim(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody InvoiceClaimDTO dto) {
        return R.ok(invoiceClaimService.updateInvoiceClaim(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(invoiceClaimService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<InvoiceClaimDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(invoiceClaimService.getInvoiceClaimDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<InvoiceClaimVO>> page(@RequestBody @Valid InvoiceClaimQueryDTO queryDTO) {
        return R.ok(invoiceClaimService.selectPage(queryDTO));
    }

    @ApiOperation(value = "开票认领生成凭证")
    @PostMapping("/invoiceGenerateVoucher")
    public R<Boolean> invoiceGenerateVoucher() {
        return R.ok(invoiceClaimService.invoiceGenerateVoucher(false));
    }

    @ApiOperation(value = "开票认领映射")
    @PostMapping("/saveMqInvoiceClaim")
    public R<Long> saveMqInvoiceClaim(@RequestBody @Valid InvoiceClaimDTO dto) {
        return R.ok(invoiceClaimService.saveMqInvoiceClaim(dto));
    }
}



