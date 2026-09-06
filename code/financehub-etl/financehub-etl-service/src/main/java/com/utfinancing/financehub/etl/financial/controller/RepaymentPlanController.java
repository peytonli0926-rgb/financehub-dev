package com.utfinancing.financehub.etl.financial.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanCYCService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanPLService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanSYCService;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanXWService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author : robjiang
 * @Date : Create in 23/01/2024
 */
@Api(tags = "对接业务系统偿还计划数据")
@Slf4j
@RestController
@RequestMapping("/repaymentPlan")
public class RepaymentPlanController {

    @Resource
    private IRepaymentPlanPLService repaymentPlanPLService;

    @Resource
    private IRepaymentPlanXWService repaymentPlanXWService;


    @Resource
    private IRepaymentPlanCYCService repaymentPlanCYCService;


    @Resource
    private IRepaymentPlanSYCService repaymentPlanSYCService;

    @PostMapping("/dataInit")
    @ApiOperation(value = "偿还计划期初数据同步")
    public R<String> dataInitFor(@RequestBody @Valid DataInitDTO params) {
        if (SystemEnum.TYPT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanPLService.dataInit(params));
        } else if (SystemEnum.XWXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanXWService.dataInit(params));
        } else if (SystemEnum.SYCXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanSYCService.dataInit(params));
        } else if (SystemEnum.CYCXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanCYCService.dataInit(params));
        } else {
            repaymentPlanPLService.dataInit(params);
            repaymentPlanXWService.dataInit(params);
            repaymentPlanSYCService.dataInit(params);
            repaymentPlanCYCService.dataInit(params);
        }
        return R.ok();
    }

    @PostMapping("/receivedAmountSync")
    @ApiOperation(value = "回笼期初数据同步")
    public R<String> receivedAmountSync(@RequestBody @Valid DataInitDTO params) {
        if (SystemEnum.TYPT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanPLService.receivedAmountSync(params));
        } else if (SystemEnum.XWXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanXWService.receivedAmountSync(params));
        } else if (SystemEnum.SYCXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanSYCService.receivedAmountSync(params));
        } else if (SystemEnum.CYCXT.getCode().equals(params.getSystemCode())) {
            return R.ok(repaymentPlanCYCService.receivedAmountSync(params));
        } else {
            repaymentPlanPLService.receivedAmountSync(params);
            repaymentPlanXWService.receivedAmountSync(params);
            repaymentPlanSYCService.receivedAmountSync(params);
            repaymentPlanCYCService.receivedAmountSync(params);
        }
        return R.ok();
    }

    @PostMapping("/taAmountSync")
    @ApiOperation(value = "TA余额同步")
    public R taAmountSync() {
        return R.ok(repaymentPlanPLService.taAmountSync());
    }
}
