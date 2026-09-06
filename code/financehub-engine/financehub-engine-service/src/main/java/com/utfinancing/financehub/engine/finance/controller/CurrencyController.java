package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.CurrencyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CurrencyDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CurrencyVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.ICurrencyService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-01-11
 * @Description :   Currency控制器实现类
 * @Modified :
 */
@Api(tags = "币别")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/currency")
public class CurrencyController {

    private final ICurrencyService  currencyService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody CurrencyDTO dto) {
        return R.ok(currencyService.saveCurrency(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody CurrencyDTO dto) {
        return R.ok(currencyService.updateCurrency(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(currencyService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<CurrencyDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(currencyService.getCurrencyDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<CurrencyVO>> page(@RequestBody @Valid CurrencyQueryDTO queryDTO) {
        return R.ok(currencyService.selectPage(queryDTO));
    }

}



