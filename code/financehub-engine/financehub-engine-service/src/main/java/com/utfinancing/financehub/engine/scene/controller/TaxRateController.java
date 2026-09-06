package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.TaxRateVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-18
 * @Description :   TaxRate控制器实现类
 * @Modified :
 */
@Api(tags = "税率配置")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/tax-rate")
public class TaxRateController {

    private final ITaxRateService  taxRateService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TaxRateSaveDTO dto) {
        return R.ok(taxRateService.saveTaxRate(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TaxRateSaveDTO dto) {
        return R.ok(taxRateService.updateTaxRate(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taxRateService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TaxRateDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taxRateService.getTaxRateDTOById(id));
    }

    @ApiOperation(value = "获取最新税率")
    @GetMapping("/getValid/{businessCode}/{fundType}")
    public R<BigDecimal> getValidTaxRate(@PathVariable("businessCode") @Valid @NotNull String businessCode,
                                         @PathVariable("fundType") @Valid @NotNull String fundType) {
        return R.ok(taxRateService.getValidTaxRateByCode(businessCode, fundType));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TaxRateVO>> page(@RequestBody @Valid TaxRateQueryDTO queryDTO) {
        return R.ok(taxRateService.selectPage(queryDTO));
    }


}



