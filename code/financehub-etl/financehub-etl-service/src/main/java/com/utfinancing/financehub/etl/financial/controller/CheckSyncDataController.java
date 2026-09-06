package com.utfinancing.financehub.etl.financial.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.service.IKingdeeCheckService;
import com.utfinancing.financehub.etl.financial.service.IMiddleCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@Api(tags = "金蝶和金蝶中间表对账数据接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/check")
public class CheckSyncDataController {

    private final IKingdeeCheckService kingdeeCheckService;

    private final IMiddleCheckService middleCheckService;

    @GetMapping("/kingdee/saveToTmp")
    @ApiOperation(value = "将与中台对账的金蝶科目余额在期间内数据保存到中台临时表")
    public R<String> saveToKingdeeTmp(@RequestParam(value = "periodCode", required = true)String periodCode) {
        return R.ok(kingdeeCheckService.saveKingdeeDataToTmp(periodCode));
    }

    @GetMapping("/middle/saveToTmp")
    @ApiOperation(value = "将与中台对账的金蝶中间表科目发生额在期间内数据保存到中台临时表")
    public R<String> saveToMiddleTmp(@RequestParam(value = "periodCode", required = true)String periodCode) {
        return R.ok(middleCheckService.saveMiddleDataToTmp(periodCode));
    }
}
