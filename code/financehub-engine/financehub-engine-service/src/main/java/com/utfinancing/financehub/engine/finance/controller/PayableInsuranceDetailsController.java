package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IPayableInsuranceDetailsService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :   PayableInsuranceDetails控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/payable-insurance-details")
public class PayableInsuranceDetailsController {

    private final IPayableInsuranceDetailsService  payableInsuranceDetailsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody PayableInsuranceDetailsDTO dto) {
        return R.ok(payableInsuranceDetailsService.savePayableInsuranceDetails(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody PayableInsuranceDetailsDTO dto) {
        return R.ok(payableInsuranceDetailsService.updatePayableInsuranceDetails(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payableInsuranceDetailsService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<PayableInsuranceDetailsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payableInsuranceDetailsService.getPayableInsuranceDetailsDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<PayableInsuranceDetailsVO>> page(@RequestBody @Valid PayableInsuranceDetailsQueryDTO queryDTO) {
        return R.ok(payableInsuranceDetailsService.selectPage(queryDTO));
    }

}



