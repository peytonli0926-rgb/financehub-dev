package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractStructureVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractStructureService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContractStructure控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/offline-contract-structure")
public class OfflineContractStructureController {

    private final IOfflineContractStructureService  offlineContractStructureService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OfflineContractStructureDTO dto) {
        return R.ok(offlineContractStructureService.saveOfflineContractStructure(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OfflineContractStructureDTO dto) {
        return R.ok(offlineContractStructureService.updateOfflineContractStructure(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(offlineContractStructureService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OfflineContractStructureDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(offlineContractStructureService.getOfflineContractStructureDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OfflineContractStructureVO>> page(@RequestBody @Valid OfflineContractStructureQueryDTO queryDTO) {
        return R.ok(offlineContractStructureService.selectPage(queryDTO));
    }

}



