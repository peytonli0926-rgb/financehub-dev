package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherEntryVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IVoucherEntryService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description :   VoucherEntry控制器实现类
 * @Modified :
 */
@Api(tags = "凭证分录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/voucher-entry")
public class VoucherEntryController {

    private final IVoucherEntryService  voucherEntryService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody VoucherEntryDTO dto) {
        return R.ok(voucherEntryService.saveVoucherEntry(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody VoucherEntryDTO dto) {
        return R.ok(voucherEntryService.updateVoucherEntry(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(voucherEntryService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<VoucherEntryDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(voucherEntryService.getVoucherEntryDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<VoucherEntryVO>> page(@RequestBody @Valid VoucherEntryQueryDTO queryDTO) {
        return R.ok(voucherEntryService.selectPage(queryDTO));
    }

    @PostMapping("/modify/{id}")
    @ApiOperation(value = "修改凭证行接口")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> modifyVoucherEntry(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody VoucherEntryDTO dto) {
        return R.ok(voucherEntryService.modifyVoucherEntry(id, dto));
    }

}



