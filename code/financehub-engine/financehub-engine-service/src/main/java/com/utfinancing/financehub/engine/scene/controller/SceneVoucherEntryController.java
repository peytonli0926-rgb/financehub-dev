package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntrySaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherEntryVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherEntryService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   SceneVoucherEntry控制器实现类
 * @Modified :
 */
@ApiIgnore()
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/voucher-entry")
public class SceneVoucherEntryController {

    private final ISceneVoucherEntryService  sceneVoucherEntryService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SceneVoucherEntrySaveDTO dto) {
        return R.ok(sceneVoucherEntryService.saveSceneVoucherEntry(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SceneVoucherEntrySaveDTO dto) {
        return R.ok(sceneVoucherEntryService.updateSceneVoucherEntry(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneVoucherEntryService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SceneVoucherEntryDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneVoucherEntryService.getSceneVoucherEntryDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SceneVoucherEntryVO>> page(@RequestBody @Valid SceneVoucherEntryQueryDTO queryDTO) {
        return R.ok(sceneVoucherEntryService.selectPage(queryDTO));
    }

}



