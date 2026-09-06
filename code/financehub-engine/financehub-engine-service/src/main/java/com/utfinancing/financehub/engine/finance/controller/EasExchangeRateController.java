package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.EasExchangeRateVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IEasExchangeRateService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-16
 * @Description :   EasExchangeRate控制器实现类
 * @Modified :
 */
@Api(tags = "汇率")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/eas-exchange-rate")
public class EasExchangeRateController {

    private final IEasExchangeRateService  easExchangeRateService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody EasExchangeRateDTO dto) {
        return R.ok(easExchangeRateService.saveEasExchangeRate(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody EasExchangeRateDTO dto) {
        return R.ok(easExchangeRateService.updateEasExchangeRate(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(easExchangeRateService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<EasExchangeRateDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(easExchangeRateService.getEasExchangeRateDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<EasExchangeRateVO>> page(@RequestBody @Valid EasExchangeRateQueryDTO queryDTO) {
        return R.ok(easExchangeRateService.selectPage(queryDTO));
    }

}



