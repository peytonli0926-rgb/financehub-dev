package com.utfinancing.financehub.engine.scene.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.scene.model.dto.AccountPeriodDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.utfinancing.financehub.engine.scene.service.IAccountPeriodService;


/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description :   AccountPeriod控制器实现类
 * @Modified :
 */
@Api(tags = "会计期间")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/scene/account-period")
public class AccountPeriodController {

    private final IAccountPeriodService  accountPeriodService;

    @PostMapping("/queryAll")
    @ApiOperation(value = "查询所有会计期间")
    public R<List<AccountPeriodDTO>> queryAll() {
        return R.ok(accountPeriodService.queryAllAccountPeriod());
    }


}



