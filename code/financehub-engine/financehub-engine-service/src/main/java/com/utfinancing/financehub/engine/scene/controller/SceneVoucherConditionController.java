package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherConditionVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherConditionService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   SceneVoucherCondition控制器实现类
 * @Modified :
 */
@ApiIgnore()
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/voucher-condition")
public class SceneVoucherConditionController {

    private final ISceneVoucherConditionService  sceneVoucherConditionService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SceneVoucherConditionSaveDTO dto) {
        return R.ok(sceneVoucherConditionService.saveSceneVoucherCondition(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SceneVoucherConditionSaveDTO dto) {
        return R.ok(sceneVoucherConditionService.updateSceneVoucherCondition(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneVoucherConditionService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SceneVoucherConditionDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneVoucherConditionService.getSceneVoucherConditionDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SceneVoucherConditionVO>> page(@RequestBody @Valid SceneVoucherConditionQueryDTO queryDTO) {
        return R.ok(sceneVoucherConditionService.selectPage(queryDTO));
    }

}



