package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeUploadRecordVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeUploadRecordService;


/**
 * @Author : robjiang
 * @Date : Create in 2025-11-20
 * @Description :   LeaseIncomeUploadRecord控制器实现类
 * @Modified :
 */
@Api(tags = "收益计提上传记录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/lease-income-upload-record")
public class LeaseIncomeUploadRecordController {

    private final ILeaseIncomeUploadRecordService  leaseIncomeUploadRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LeaseIncomeUploadRecordDTO dto) {
        return R.ok(leaseIncomeUploadRecordService.saveLeaseIncomeUploadRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LeaseIncomeUploadRecordDTO dto) {
        return R.ok(leaseIncomeUploadRecordService.updateLeaseIncomeUploadRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(leaseIncomeUploadRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LeaseIncomeUploadRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(leaseIncomeUploadRecordService.getLeaseIncomeUploadRecordDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LeaseIncomeUploadRecordVO>> page(@RequestBody @Valid LeaseIncomeUploadRecordQueryDTO queryDTO) {
        return R.ok(leaseIncomeUploadRecordService.selectPage(queryDTO));
    }

}



