package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankMappingVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-12-21
 * @Description :   FundBusinessSystemEbankMapping控制器实现类
 * @Modified :
 */
@Api(tags = "资金系统、业务系统网银编号映射表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/fund-business-system-ebank-mapping")
public class FundBusinessSystemEbankMappingController {

    private final IFundBusinessSystemEbankMappingService  fundBusinessSystemEbankMappingService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FundBusinessSystemEbankMappingDTO dto) {
        return R.ok(fundBusinessSystemEbankMappingService.saveFundBusinessSystemEbankMapping(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FundBusinessSystemEbankMappingDTO dto) {
        return R.ok(fundBusinessSystemEbankMappingService.updateFundBusinessSystemEbankMapping(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundBusinessSystemEbankMappingService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FundBusinessSystemEbankMappingDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundBusinessSystemEbankMappingService.getFundBusinessSystemEbankMappingDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FundBusinessSystemEbankMappingVO>> page(@RequestBody @Valid FundBusinessSystemEbankMappingQueryDTO queryDTO) {
        return R.ok(fundBusinessSystemEbankMappingService.selectPage(queryDTO));
    }

}



