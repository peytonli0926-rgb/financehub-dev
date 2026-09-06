package com.utfinancing.financehub.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.api.model.SysInternalRole;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleQuery;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleVO;
import com.utfinancing.financehub.common.core.dto.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.utfinancing.financehub.admin.service.ISysInternalRoleService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description :   SysInternalRole控制器实现类
 * @Modified :
 */
@Api(tags = "中台内部角色信息")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-internal-role")
public class SysInternalRoleController {

    private final ISysInternalRoleService  sysInternalRoleService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SysInternalRole dto) {
        return R.ok(sysInternalRoleService.saveSysInternalRole(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SysInternalRole dto) {
        return R.ok(sysInternalRoleService.updateSysInternalRole(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleService.removeByRoleId(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SysInternalRole> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleService.getSysInternalRoleDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SysInternalRoleVO>> page(@RequestBody @Valid SysInternalRoleQuery queryDTO) {
        return R.ok(sysInternalRoleService.selectPage(queryDTO));
    }

}



