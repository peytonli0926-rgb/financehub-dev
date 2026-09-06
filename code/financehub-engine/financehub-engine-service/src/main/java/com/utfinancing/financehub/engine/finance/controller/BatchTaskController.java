package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchTaskVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IBatchTaskService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-06-25
 * @Description :   BatchTask控制器实现类
 * @Modified :
 */
@Api(tags = "批量任务")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/batch-task")
public class BatchTaskController {

    private final IBatchTaskService  batchTaskService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody BatchTaskDTO dto) {
        return R.ok(batchTaskService.saveBatchTask(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BatchTaskDTO dto) {
        return R.ok(batchTaskService.updateBatchTask(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(batchTaskService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<BatchTaskDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(batchTaskService.getBatchTaskDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<BatchTaskVO>> page(@RequestBody @Valid BatchTaskQueryDTO queryDTO) {
        return R.ok(batchTaskService.selectPage(queryDTO));
    }


}



