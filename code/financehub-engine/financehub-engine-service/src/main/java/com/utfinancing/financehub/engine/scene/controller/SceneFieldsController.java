package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneFieldsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.ISceneFieldsService;


/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   SceneFields控制器实现类
 * @Modified :
 */
@Api(tags = "业务场景接口字段")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/fields")
public class SceneFieldsController {

    private final ISceneFieldsService  sceneFieldsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SceneFieldsSaveDTO dto) {
        return R.ok(sceneFieldsService.saveSceneFields(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SceneFieldsSaveDTO dto) {
        return R.ok(sceneFieldsService.updateSceneFields(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneFieldsService.removeSceneField(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SceneFieldsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sceneFieldsService.getSceneFieldsDTOById(id));
    }


    @ApiOperation(value = "查询所有去重字段列表")
    @GetMapping("/getAllFields")
    public R<List<SceneFieldsDTO>> getAllFields() {
        return R.ok(sceneFieldsService.listAllSceneFieldsDistinct());
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SceneFieldsVO>> page(@RequestBody @Valid SceneFieldsQueryDTO queryDTO) {
        return R.ok(sceneFieldsService.selectPage(queryDTO));
    }

    /**
     * 根据sceneId获取字段树，用于规则配置页面抽屉的字段
     * @param sceneId
     * @return
     */
    @ApiOperation(value = "根据sceneId获取字段列表")
    @GetMapping("/tree/{sceneId}")
    @ApiImplicitParam(paramType = "path", name = "sceneId", value = "sceneId", required = true, type = "long", dataTypeClass = Long.class)
    public R<List<EditorOptionDTO>> tree(@PathVariable("sceneId") @Valid @NotNull Long sceneId) {
        return R.ok(sceneFieldsService.getTreeBySceneId(sceneId));
    }


    /**
     * 根据sceneId获取查询编辑器选项列表，用于表达式编辑器
     * @param sceneId
     * @return
     */
    @ApiOperation(value = "查询表达式编辑器选项列表")
    @GetMapping("/listEditorOption/{sceneId}")
    @ApiImplicitParam(paramType = "path", name = "sceneId", value = "sceneId", required = true, type = "long", dataTypeClass = Long.class)
    public R<List<EditorOptionDTO>> listEditorOption(@PathVariable("sceneId") @Valid @NotNull Long sceneId) {
        return R.ok(sceneFieldsService.listEditorOption(sceneId));
    }

}



