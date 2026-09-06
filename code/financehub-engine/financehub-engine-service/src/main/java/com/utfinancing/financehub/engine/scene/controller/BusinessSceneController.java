package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessSceneVO;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.IBusinessSceneService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :   BusinessScene控制器实现类
 * @Modified :
 */
@Api(tags = "业务与场景关联配置")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/business-scene")
public class BusinessSceneController {

    private final IBusinessSceneService  businessSceneService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody BusinessSceneSaveDTO dto) {
        return R.ok(businessSceneService.saveBusinessScene(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BusinessSceneDTO dto) {
        return R.ok(businessSceneService.updateBusinessScene(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(businessSceneService.removeByBusinessId(id));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete/batch")
    public R<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            businessSceneService.removeByBusinessId(id);
        }
        return R.ok(Boolean.TRUE);
    }

    @ApiIgnore()
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<BusinessSceneDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(businessSceneService.getBusinessSceneDTOById(id));
    }

    @ApiIgnore()
    @PostMapping("/page")
    public R<IPage<BusinessSceneVO>> page(@RequestBody @Valid BusinessSceneQueryDTO queryDTO) {
        return R.ok(businessSceneService.selectPage(queryDTO));
    }

}



