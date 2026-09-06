package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceCheckQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-23
 * @Description :   ContractBalance控制器实现类
 * @Modified :
 */
@Api(tags = "统计报表-租赁大表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/contract-balance")
public class ContractBalanceController {

    private final IContractBalanceService  contractBalanceService;

    @ApiOperation(value = "getLastContractBalanceMap")
    @GetMapping("/getLastContractBalanceMap")
    public R<Map<String, Object>> getMap(@RequestParam String businessCode, @RequestParam String contractCode, @RequestParam String clientCode,@RequestParam String orgId,@RequestParam String billContractCode) {
        return R.ok(contractBalanceService.getLastBalanceMap(businessCode, clientCode, contractCode,orgId,billContractCode));
    }

    @ApiOperation(value = "分页查询（合同业务交易信息）")
    @PostMapping("/page")
    public R<IPage<ContractBalanceVO>> page(@RequestBody @Valid ContractBalanceQueryDTO queryDTO) {
        return R.ok(contractBalanceService.selectPage(queryDTO));
    }

    @ApiOperation(value = "校验分页查询")
    @PostMapping("/checkPage")
    public R<IPage<ContractBalanceVO>> checkPage(@RequestBody @Valid ContractBalanceCheckQueryDTO queryDTO) {
        return R.ok(contractBalanceService.selectCheckPage(queryDTO));
    }

}



