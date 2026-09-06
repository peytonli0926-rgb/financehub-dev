package com.utfinancing.financehub.admin.controller;

import com.github.pagehelper.IPage;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.core.web.controller.BaseController;
import com.utfinancing.financehub.common.core.web.domain.AjaxResult;
import com.utfinancing.financehub.common.core.web.page.TableDataInfo;
//import com.utfinancing.financehub.common.log.annotation.Log;
//import com.utfinancing.financehub.common.log.enums.BusinessType;
import com.utfinancing.financehub.common.log.annotation.Log;
import com.utfinancing.financehub.common.log.enums.BusinessType;
//import com.utfinancing.financehub.common.security.annotation.RequiresPermissions;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.admin.api.model.SysDictType;
import com.utfinancing.financehub.admin.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Api(tags = "数据字典类型接口")
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "分页查询")
//    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @ApiOperation(value = "查询字典类型详细")
//    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典类型")
//    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getLoginUser().getStaffCode());
        dict.setUpdateBy(SecurityUtils.getLoginUser().getStaffCode());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @ApiOperation(value = "修改字典类型")
//    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/{dictId}")
    @ApiImplicitParam(paramType = "path", name = "dictId", value = "dictId", required = true, type = "long", dataTypeClass = Long.class)
    public AjaxResult update(@PathVariable("dictId") @Valid @NotNull Long dictId, @Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getLoginUser().getStaffCode());
        return toAjax(dictTypeService.updateDictType(dictId, dict));
    }

    /**
     * 删除字典类型
     */
    @ApiOperation(value = "删除字典类型")
//    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @ApiOperation(value = "刷新字典缓存")
//    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @ApiOperation(value = "获取字典选择框列表")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
}
