package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeDetailsService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-13
 * @Description :   LeaseIncomeDetails控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/lease-income-details")
public class LeaseIncomeDetailsController {

    private final ILeaseIncomeDetailsService  leaseIncomeDetailsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LeaseIncomeDetailsDTO dto) {
        return R.ok(leaseIncomeDetailsService.saveLeaseIncomeDetails(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LeaseIncomeDetailsDTO dto) {
        return R.ok(leaseIncomeDetailsService.updateLeaseIncomeDetails(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(leaseIncomeDetailsService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LeaseIncomeDetailsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(leaseIncomeDetailsService.getLeaseIncomeDetailsDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LeaseIncomeDetailsVO>> page(@RequestBody @Valid LeaseIncomeDetailsQueryDTO queryDTO) {
        return R.ok(leaseIncomeDetailsService.selectPage(queryDTO));
    }

}



