package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IServiceFeeDetailsService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :   ServiceFeeDetails控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee-details")
public class ServiceFeeDetailsController {

    private final IServiceFeeDetailsService  serviceFeeDetailsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ServiceFeeDetailsDTO dto) {
        return R.ok(serviceFeeDetailsService.saveServiceFeeDetails(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ServiceFeeDetailsDTO dto) {
        return R.ok(serviceFeeDetailsService.updateServiceFeeDetails(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeeDetailsService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ServiceFeeDetailsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeeDetailsService.getServiceFeeDetailsDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ServiceFeeDetailsVO>> page(@RequestBody @Valid ServiceFeeDetailsQueryDTO queryDTO) {
        return R.ok(serviceFeeDetailsService.selectPage(queryDTO));
    }

}



