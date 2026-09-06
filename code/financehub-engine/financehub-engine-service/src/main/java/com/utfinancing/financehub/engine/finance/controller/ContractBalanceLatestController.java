package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceLatestVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceLatestService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-30
 * @Description :   ContractBalanceLatest控制器实现类
 * @Modified :
 */
@Api(tags = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/contract-balance-latest")
public class ContractBalanceLatestController {

    private final IContractBalanceLatestService  contractBalanceLatestService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ContractBalanceLatestDTO dto) {
        return R.ok(contractBalanceLatestService.saveContractBalanceLatest(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ContractBalanceLatestDTO dto) {
        return R.ok(contractBalanceLatestService.updateContractBalanceLatest(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractBalanceLatestService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractBalanceLatestDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractBalanceLatestService.getContractBalanceLatestDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ContractBalanceLatestVO>> page(@RequestBody @Valid ContractBalanceLatestQueryDTO queryDTO) {
        return R.ok(contractBalanceLatestService.selectPage(queryDTO));
    }

}



