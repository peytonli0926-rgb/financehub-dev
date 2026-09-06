package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.ITailDifferenceAdjustmentDetailService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description :   TailDifferenceAdjustmentDetail控制器实现类
 * @Modified :
 */
@Api(tags = "尾差调整详情")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/tail-difference-adjustment-detail")
public class TailDifferenceAdjustmentDetailController {

    private final ITailDifferenceAdjustmentDetailService  tailDifferenceAdjustmentDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TailDifferenceAdjustmentDetailDTO dto) {
        return R.ok(tailDifferenceAdjustmentDetailService.saveTailDifferenceAdjustmentDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TailDifferenceAdjustmentDetailDTO dto) {
        return R.ok(tailDifferenceAdjustmentDetailService.updateTailDifferenceAdjustmentDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(tailDifferenceAdjustmentDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TailDifferenceAdjustmentDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(tailDifferenceAdjustmentDetailService.getTailDifferenceAdjustmentDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TailDifferenceAdjustmentDetailVO>> page(@RequestBody @Valid TailDifferenceAdjustmentDetailQueryDTO queryDTO) {
        return R.ok(tailDifferenceAdjustmentDetailService.selectPage(queryDTO));
    }

}



