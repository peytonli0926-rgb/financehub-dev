package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionUploadTaskVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IImpairmentProvisionUploadTaskService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-28
 * @Description :   ImpairmentProvisionUploadTask控制器实现类
 * @Modified :
 */
@Api(tags = "减值计提上传任务")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/impairment-provision-upload-task")
public class ImpairmentProvisionUploadTaskController {

    private final IImpairmentProvisionUploadTaskService  impairmentProvisionUploadTaskService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ImpairmentProvisionUploadTaskDTO dto) {
        return R.ok(impairmentProvisionUploadTaskService.saveImpairmentProvisionUploadTask(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ImpairmentProvisionUploadTaskDTO dto) {
        return R.ok(impairmentProvisionUploadTaskService.updateImpairmentProvisionUploadTask(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentProvisionUploadTaskService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ImpairmentProvisionUploadTaskDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentProvisionUploadTaskService.getImpairmentProvisionUploadTaskDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ImpairmentProvisionUploadTaskVO>> page(@RequestBody @Valid ImpairmentProvisionUploadTaskQueryDTO queryDTO) {
        return R.ok(impairmentProvisionUploadTaskService.selectPage(queryDTO));
    }

}



