package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;
import com.utfinancing.financehub.engine.scene.service.IBusinessSceneService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;


/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :   Business控制器实现类
 * @Modified :
 */
@Api(tags = "业务配置")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/business")
public class BusinessController {

    private final IBusinessService  businessService;
    private final IBusinessSceneService businessSceneService;


    @ApiOperation(value = "业务配置列表")
    @PostMapping("/list")
    public R<List<BusinessVO>> list(@RequestBody @Valid BusinessQueryDTO queryDTO) {
        return R.ok(businessService.selectAll(queryDTO));
    }

    @ApiOperation(value = "获取该配置下的场景")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<List<BusinessSceneDTO>> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(businessSceneService.getBusinessSceneDTOsByBusinessId(id));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BusinessDTO dto) {
        return R.ok(businessService.updateBusiness(id, dto));
    }

}



