package com.utfinancing.financehub.engine.rule.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.xxlJob.DataExecutionJobHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author : lixin
 * @Date : Create in 2024-02-25
 * @Description :   DataExecutionTask控制器实现类
 * @Modified :
 */
@Api(tags = "业务数据执行会计引擎接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule/data-execution-task")
public class DataExecutionTaskController {

    private final DataExecutionJobHandler dataExecutionJobHandler;


    @PostMapping("/executeSystemData")
    @ApiOperation(value = "业务数据执行会计引擎")
    public R<Boolean> executeSystemData(@RequestParam("systemCode") String systemCode) {
        dataExecutionJobHandler.executeSystemData(systemCode);
        return R.ok(Boolean.TRUE);
    }

    @PostMapping("/executeIntefaceData")
    @ApiOperation(value = "接口表执行会计引擎")
    public R<Boolean> executeIntefaceData() {
        dataExecutionJobHandler.executeIntefaceData();
        return R.ok(Boolean.TRUE);
    }



}



