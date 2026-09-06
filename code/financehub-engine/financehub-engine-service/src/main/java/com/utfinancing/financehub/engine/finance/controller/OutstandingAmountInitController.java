package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutstandingAmountInitVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IOutstandingAmountInitService;


/**
 * @Author : robjiang
 * @Date : Create in 2025-05-15
 * @Description :   OutstandingAmountInit控制器实现类
 * @Modified :
 */
@Api(tags = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/outstanding-amount-init")
public class OutstandingAmountInitController {

    private final IOutstandingAmountInitService  outstandingAmountInitService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OutstandingAmountInitDTO dto) {
        return R.ok(outstandingAmountInitService.saveOutstandingAmountInit(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OutstandingAmountInitDTO dto) {
        return R.ok(outstandingAmountInitService.updateOutstandingAmountInit(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(outstandingAmountInitService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OutstandingAmountInitDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(outstandingAmountInitService.getOutstandingAmountInitDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OutstandingAmountInitVO>> page(@RequestBody @Valid OutstandingAmountInitQueryDTO queryDTO) {
        return R.ok(outstandingAmountInitService.selectPage(queryDTO));
    }

}



