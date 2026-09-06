package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.MappingDTO;
import com.utfinancing.financehub.engine.scene.model.vo.FieldMappingVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-27
 * @Description :   FieldMapping控制器实现类
 * @Modified :
 */
@Api(tags = "字段映射表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/field-mapping")
public class FieldMappingController {

    private final IFieldMappingService  fieldMappingService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FieldMappingSaveDTO dto) {
        return R.ok(fieldMappingService.saveFieldMapping(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FieldMappingSaveDTO dto) {
        return R.ok(fieldMappingService.updateFieldMapping(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fieldMappingService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FieldMappingDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fieldMappingService.getFieldMappingDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FieldMappingVO>> page(@RequestBody @Valid FieldMappingQueryDTO queryDTO) {
        return R.ok(fieldMappingService.selectPage(queryDTO));
    }

    @ApiOperation(value = "清空缓存")
    @PostMapping("/cleanCache")
    public R<Boolean> cleanCache() {
        fieldMappingService.cleanCache();
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation(value = "查询所有映射关系")
    @PostMapping("/queryAllMapping")
    public R<Map<String, Map<String, List<MappingDTO>>>> queryAllMapping(){
        return R.ok(fieldMappingService.queryAllFieldMapping());
    }

    @ApiOperation(value = "根据条件查询映射表数据")
    @PostMapping("/selectFieldMappingByCondition")
    public R<List<FieldMappingApiVO>> selectFieldMappingByCondition(FieldMappingApiDTO queryDTO){
        return R.ok(fieldMappingService.selectFieldMappingByCondition(queryDTO));
    }

}



