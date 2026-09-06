package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.ISceneService;


/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   Scene控制器实现类
 * @Modified :
 */
@Api(tags = "业务场景")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene")
public class SceneController {

    private final ISceneService  sceneService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SceneSaveDTO dto) {
        return R.ok(sceneService.saveScene(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SceneSaveDTO dto) {
        return R.ok(sceneService.updateScene(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SceneDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneService.getSceneDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SceneVO>> page(@RequestBody @Valid SceneQueryDTO queryDTO) {
        return R.ok(sceneService.selectPage(queryDTO));
    }

    @ApiOperation(value = "查询所有场景列表")
    @PostMapping("/list")
    public R<List<SceneVO>> list(@RequestBody @Valid SceneQueryDTO queryDTO) {
        return R.ok(sceneService.selectAll(queryDTO));
    }

    @ApiOperation(value = "获取场景对应的凭证规则")
    @GetMapping("/rule/{sceneId}")
    @ApiImplicitParam(paramType = "path", name = "sceneId", value = "sceneId", required = true, type = "long", dataTypeClass = Long.class)
    public R<SceneRuleConfigDTO> getRule(@PathVariable("sceneId") @Valid @NotNull Long sceneId) {
        return R.ok(sceneService.getSceneRuleConfigById(sceneId));
    }

    @PostMapping("/rule/save")
    @ApiOperation(value = "保存凭证规则")
    public R<Boolean> saveRule(@Valid @RequestBody SceneRuleConfigSaveDTO dto) {
        return R.ok(sceneService.saveSceneRuleConfig(dto));
    }
}



