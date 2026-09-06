package com.utfinancing.financehub.etl.financial.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.etl.financial.model.vo.DataExecutionTaskVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.etl.financial.service.IDataExecutionTaskService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-04-07
 * @Description :   DataExecutionTask控制器实现类
 * @Modified :
 */
@Api(tags = "业务系统数据执行任务表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/data-execution-task")
public class DataExecutionTaskController {

    private final IDataExecutionTaskService  dataExecutionTaskService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody DataExecutionTaskDTO dto) {
        return R.ok(dataExecutionTaskService.saveDataExecutionTask(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody DataExecutionTaskDTO dto) {
        return R.ok(dataExecutionTaskService.updateDataExecutionTask(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(dataExecutionTaskService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<DataExecutionTaskDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(dataExecutionTaskService.getDataExecutionTaskDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<DataExecutionTaskVO>> page(@RequestBody @Valid DataExecutionTaskQueryDTO queryDTO) {
        return R.ok(dataExecutionTaskService.selectPage(queryDTO));
    }

}



