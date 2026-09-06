package com.utfinancing.financehub.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserQueryDTO;
import com.utfinancing.financehub.admin.model.dto.SysInternalUserDTO;
import com.utfinancing.financehub.admin.model.vo.SysInternalUserVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.utfinancing.financehub.admin.service.ISysInternalUserService;

import java.util.List;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :   SysInternalUser控制器实现类
 * @Modified :
 */
@Api(tags = "中台内部用户信息")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-internal-user")
public class SysInternalUserController {

    private final ISysInternalUserService  sysInternalUserService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SysInternalUserDTO dto) {
        return R.ok(sysInternalUserService.saveSysInternalUser(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SysInternalUserDTO dto) {
        return R.ok(sysInternalUserService.updateSysInternalUser(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalUserService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SysInternalUserDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sysInternalUserService.getSysInternalUserDTOById(id));
    }

    @ApiOperation(value = "用户分页查询")
    @PostMapping("/page")
    public R<IPage<SysInternalUserVO>> page(@RequestBody @Valid SysInternalUserQueryDTO queryDTO) {
        return R.ok(sysInternalUserService.selectPage(queryDTO));
    }

    @ApiOperation(value = "用户列表查询")
    @PostMapping("/listByCondition")
    public R<List<SysInternalUserVO>> listByCondition(@RequestBody @Valid SysInternalUserQueryDTO queryDTO) {
        return R.ok(sysInternalUserService.listByCondition(queryDTO));
    }

    @ApiOperation(value = "根据用户编码获取复核角色下下级人员信息")
    @GetMapping("/getReviewSubUserByUserCode/{userCode}")
        public R<List<SysInternalUser>> getReviewSubUserByUserCode(@PathVariable("userCode") @Valid @NotNull String userCode) {
        return R.ok(sysInternalUserService.getReviewSubUserByUserCode(userCode));
    }

}



