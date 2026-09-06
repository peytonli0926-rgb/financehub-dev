package com.utfinancing.financehub.admin.controller;

import com.utfinancing.financehub.common.core.constant.UserConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.core.web.controller.BaseController;
import com.utfinancing.financehub.common.core.web.domain.AjaxResult;
import com.utfinancing.financehub.common.core.web.page.TableDataInfo;
import com.utfinancing.financehub.common.log.annotation.Log;
import com.utfinancing.financehub.common.log.enums.BusinessType;
//import com.utfinancing.financehub.common.security.annotation.RequiresPermissions;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.admin.service.ISysDictDataService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Api(tags = "数据字典数据接口")
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "分页查询")
//    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @ApiOperation(value = "获取所有字典数据map")
//    @RequiresPermissions("system:dict:list")
    @GetMapping("/map")
    public R key() {
        Map map = dictDataService.mapAllDictData();
        return R.ok(map);
    }

    @ApiOperation(value = "导出")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @ApiOperation(value = "查询字典数据详细")
//    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation(value = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType, @RequestParam(value = "status", required = false) String status) {
        if (StringUtils.isBlank(status)) {
            status = UserConstants.DICT_NORMAL;
        }
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType, status);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典数据")
//    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getLoginUser().getStaffCode());
        dict.setUpdateBy(SecurityUtils.getLoginUser().getStaffCode());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation(value = "修改保存字典数据")
//    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping("/{dictCode}")
    @ApiImplicitParam(paramType = "path", name = "dictCode", value = "dictCode", required = true, type = "long", dataTypeClass = Long.class)
    public AjaxResult update(@PathVariable("dictCode") @Valid @NotNull Long dictCode, @Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getLoginUser().getStaffCode());
        return toAjax(dictDataService.updateDictData(dictCode, dict));
    }

    /**
     * 删除字典类型
     */
    @ApiOperation(value = "删除字典数据")
//    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
