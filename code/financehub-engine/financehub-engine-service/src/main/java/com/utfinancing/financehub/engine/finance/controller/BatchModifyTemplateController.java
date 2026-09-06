package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchModifyTemplateVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IBatchModifyTemplateService;


/**
 * @Author : robjiang
 * @Date : Create in 2025-06-23
 * @Description :   BatchModifyTemplate控制器实现类
 * @Modified :
 */
@Api(tags = "未确认收款-对账表批量修改上传模板表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/batch-modify-template")
public class BatchModifyTemplateController {

    private final IBatchModifyTemplateService  batchModifyTemplateService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody BatchModifyTemplateDTO dto) {
        return R.ok(batchModifyTemplateService.saveBatchModifyTemplate(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BatchModifyTemplateDTO dto) {
        return R.ok(batchModifyTemplateService.updateBatchModifyTemplate(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(batchModifyTemplateService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<BatchModifyTemplateDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(batchModifyTemplateService.getBatchModifyTemplateDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<BatchModifyTemplateVO>> page(@RequestBody @Valid BatchModifyTemplateQueryDTO queryDTO) {
        return R.ok(batchModifyTemplateService.selectPage(queryDTO));
    }

}



