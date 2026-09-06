package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingDTO;
import com.utfinancing.financehub.engine.finance.model.vo.HyFullOnlineBankBatchNoMappingVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IHyFullOnlineBankBatchNoMappingService;


/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description :   HyFullOnlineBankBatchNoMapping控制器实现类
 * @Modified :
 */
@Api(tags = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/hy-full-online-bank-batch-no-mapping")
public class HyFullOnlineBankBatchNoMappingController {

    private final IHyFullOnlineBankBatchNoMappingService  hyFullOnlineBankBatchNoMappingService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody HyFullOnlineBankBatchNoMappingDTO dto) {
        return R.ok(hyFullOnlineBankBatchNoMappingService.saveHyFullOnlineBankBatchNoMapping(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody HyFullOnlineBankBatchNoMappingDTO dto) {
        return R.ok(hyFullOnlineBankBatchNoMappingService.updateHyFullOnlineBankBatchNoMapping(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(hyFullOnlineBankBatchNoMappingService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<HyFullOnlineBankBatchNoMappingDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(hyFullOnlineBankBatchNoMappingService.getHyFullOnlineBankBatchNoMappingDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<HyFullOnlineBankBatchNoMappingVO>> page(@RequestBody @Valid HyFullOnlineBankBatchNoMappingQueryDTO queryDTO) {
        return R.ok(hyFullOnlineBankBatchNoMappingService.selectPage(queryDTO));
    }

}



