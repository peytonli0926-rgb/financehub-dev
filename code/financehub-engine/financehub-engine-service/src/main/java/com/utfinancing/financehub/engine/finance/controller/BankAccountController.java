package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BankAccountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BankAccountVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-12-06
 * @Description :   BankAccount控制器实现类
 * @Modified :
 */
@Api(tags = "银行账户")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/bank-account")
public class BankAccountController {

    private final IBankAccountService  bankAccountService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody BankAccountDTO dto) {
        return R.ok(bankAccountService.saveBankAccount(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BankAccountDTO dto) {
        return R.ok(bankAccountService.updateBankAccount(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(bankAccountService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<BankAccountDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(bankAccountService.getBankAccountDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<BankAccountVO>> page(@RequestBody @Valid BankAccountQueryDTO queryDTO) {
        return R.ok(bankAccountService.selectPage(queryDTO));
    }

}



