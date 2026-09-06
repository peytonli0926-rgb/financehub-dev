package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterDetailVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IRentRegisterDetailService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description :   RentRegisterDetail控制器实现类
 * @Modified :
 */
@Api(tags = "出租登记-租金计划")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/rent-register-detail")
public class RentRegisterDetailController {

    private final IRentRegisterDetailService  rentRegisterDetailService;
/*
    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RentRegisterDetailDTO dto) {
        return R.ok(rentRegisterDetailService.saveRentRegisterDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RentRegisterDetailDTO dto) {
        return R.ok(rentRegisterDetailService.updateRentRegisterDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rentRegisterDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RentRegisterDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rentRegisterDetailService.getRentRegisterDetailDTOById(id));
    }*/

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<RentRegisterDetailVO>> page(@RequestBody @Valid RentRegisterDetailQueryDTO queryDTO) {
        return R.ok(rentRegisterDetailService.selectPage(queryDTO));
    }

}



