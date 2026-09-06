package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.LprDataDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LprDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LprDataVO;
import com.utfinancing.financehub.engine.finance.service.ILprDataService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :   LprData控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/lpr-data")
public class LprDataController {

    private final ILprDataService  lprDataService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LprDataDTO dto) {
        return R.ok(lprDataService.saveLprData(dto));
    }


    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LprDataDTO dto) {
        return R.ok(lprDataService.updateLprData(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(lprDataService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LprDataDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(lprDataService.getLprDataDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LprDataVO>> page(@RequestBody @Valid LprDataQueryDTO queryDTO) {
        return R.ok(lprDataService.selectPage(queryDTO));
    }

}



