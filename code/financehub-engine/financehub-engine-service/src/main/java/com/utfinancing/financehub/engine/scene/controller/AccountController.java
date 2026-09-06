package com.utfinancing.financehub.engine.scene.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountDTO;
import com.utfinancing.financehub.engine.scene.model.dto.AccountSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.AccountVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.IAccountService;


/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   Account控制器实现类
 * @Modified :
 */
@Api(tags = "科目")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/account")
public class AccountController {

    private final IAccountService  accountService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody AccountSaveDTO dto) {
        return R.ok(accountService.saveAccount(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody AccountSaveDTO dto) {
        return R.ok(accountService.updateAccount(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(accountService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<AccountDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(accountService.getAccountDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<AccountVO>> page(@RequestBody @Valid AccountQueryDTO queryDTO) {
        return R.ok(accountService.selectPage(queryDTO));
    }


    @ApiOperation(value = "查询所有科目编码和名称")
    @PostMapping("/listAll")
    public R<List<AccountVO>> listAll() {
        return R.ok(accountService.queryAllAccount());
    }
}



