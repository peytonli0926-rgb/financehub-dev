package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractMonthVO;
import com.utfinancing.financehub.engine.finance.service.IContractMonthService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2025-04-21
 * @Description :   ContractMonth控制器实现类
 * @Modified :
 */
@Api(tags = "合同月表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/engine/contract-month")
public class ContractMonthController {

    private final IContractMonthService contractMonthService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ContractMonthDTO dto) {
        return R.ok(contractMonthService.saveContractMonth(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ContractMonthDTO dto) {
        return R.ok(contractMonthService.updateContractMonth(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractMonthService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractMonthDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractMonthService.getContractMonthDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ContractMonthVO>> page(@RequestBody @Valid ContractMonthQueryDTO queryDTO) {
        return R.ok(contractMonthService.selectPage(queryDTO));
    }

}



