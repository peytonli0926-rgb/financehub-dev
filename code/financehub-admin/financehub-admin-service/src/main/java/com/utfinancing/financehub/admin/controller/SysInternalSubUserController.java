package com.utfinancing.financehub.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalSubUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalSubUserVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.admin.service.ISysInternalSubUserService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :   SysInternalSubUser控制器实现类
 * @Modified :
 */
@Api(tags = "中台内部分配管理用户")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-internal-sub-user")
public class SysInternalSubUserController {

    private final ISysInternalSubUserService  sysInternalSubUserService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SysInternalSubUserDTO dto) {
        return R.ok(sysInternalSubUserService.saveSysInternalSubUser(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SysInternalSubUserDTO dto) {
        return R.ok(sysInternalSubUserService.updateSysInternalSubUser(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalSubUserService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SysInternalSubUserDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalSubUserService.getSysInternalSubUserDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SysInternalSubUserVO>> page(@RequestBody @Valid SysInternalSubUserQueryDTO queryDTO) {
        return R.ok(sysInternalSubUserService.selectPage(queryDTO));
    }

}



