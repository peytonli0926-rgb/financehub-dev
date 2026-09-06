package com.utfinancing.financehub.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrg;
import com.utfinancing.financehub.admin.api.model.SysInternalRoleOrgQuery;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleOrgVO;
import com.utfinancing.financehub.common.core.dto.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.utfinancing.financehub.admin.service.ISysInternalRoleOrgService;

import java.util.List;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-16
 * @Description :   SysInternalRoleOrg控制器实现类
 * @Modified :
 */
@Api(tags = "中台内部数据权限信息")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-internal-role-org")
public class SysInternalRoleOrgController {

    private final ISysInternalRoleOrgService  sysInternalRoleOrgService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SysInternalRoleOrg dto) {
        return R.ok(sysInternalRoleOrgService.saveSysInternalRoleOrg(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SysInternalRoleOrg dto) {
        return R.ok(sysInternalRoleOrgService.updateSysInternalRoleOrg(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleOrgService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SysInternalRoleOrg> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleOrgService.getSysInternalRoleOrgDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SysInternalRoleOrgVO>> page(@RequestBody @Valid SysInternalRoleOrgQuery queryDTO) {
        return R.ok(sysInternalRoleOrgService.selectPage(queryDTO));
    }

    @ApiOperation(value = "根据用户编码获取数据权限")
    @GetMapping("/getOrgByUserCode/{userCode}")
    public R<List<String>> getOrgByUserCode(@PathVariable("userCode") String userCode) {
        return R.ok(sysInternalRoleOrgService.getOrgByUserCode(userCode));
    }

}



