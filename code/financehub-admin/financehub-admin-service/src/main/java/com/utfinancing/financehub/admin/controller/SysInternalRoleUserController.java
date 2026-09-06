package com.utfinancing.financehub.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalRoleUserQueryDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalRoleUserVO;
import com.utfinancing.financehub.common.core.dto.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.utfinancing.financehub.admin.service.ISysInternalRoleUserService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-18
 * @Description :   SysInternalRoleUser控制器实现类
 * @Modified :
 */
@Api(tags = "中台内部角色下用户信息")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-internal-role-user")
public class SysInternalRoleUserController {

    private final ISysInternalRoleUserService  sysInternalRoleUserService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SysInternalRoleUserDTO dto) {
        return R.ok(sysInternalRoleUserService.saveSysInternalRoleUser(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SysInternalRoleUserDTO dto) {
        return R.ok(sysInternalRoleUserService.updateSysInternalRoleUser(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleUserService.removeUserById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SysInternalRoleUserDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalRoleUserService.getSysInternalRoleUserDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SysInternalRoleUserVO>> page(@RequestBody @Valid SysInternalRoleUserQueryDTO queryDTO) {
        return R.ok(sysInternalRoleUserService.selectPage(queryDTO));
    }

}



