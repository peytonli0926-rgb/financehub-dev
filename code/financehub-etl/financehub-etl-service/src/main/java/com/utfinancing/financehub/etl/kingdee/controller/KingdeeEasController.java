package com.utfinancing.financehub.etl.kingdee.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeEasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@Api(tags = "金蝶数据查询查询接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/kingdee/eas")
public class KingdeeEasController {

    private final IKingdeeEasService kingdeeEasService;

    @GetMapping("/getCurrentPeriodCode")
    @ApiOperation(value = "查询当前会计期间")
    public R<Integer> getCurrentPeriodCode(@RequestParam(value = "orgId", required = true)String orgId) {
        return R.ok(kingdeeEasService.getCurrentPeriodCode(orgId));
    }
    @GetMapping("/getCurrentPeriodCodeAll")
    @ApiOperation(value = "查询当前会计期间-所有")
    public R<List<Map<String,String>>> getCurrentPeriodCodeAll() {
        return R.ok(kingdeeEasService.getCurrentPeriodCodeAll());
    }

}
