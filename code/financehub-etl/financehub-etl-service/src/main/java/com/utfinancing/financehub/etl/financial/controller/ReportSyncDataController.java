package com.utfinancing.financehub.etl.financial.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceService;
import com.utfinancing.financehub.etl.financial.service.IKingdeeCheckService;
import com.utfinancing.financehub.etl.financial.service.IMiddleCheckService;
import com.utfinancing.financehub.etl.financial.service.IReportFinanceDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@Api(tags = "中台核对报表模块数据接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/report")
public class ReportSyncDataController {

    @Resource
    private IReportFinanceDataService reportFinanceDataService;

    @Resource
    private IAccountAssistBalanceService accountAssistBalanceService;


    @PostMapping("/assistantAndDetailBalance/execute")
    @ApiOperation(value = "将辅助余额表和台账明细表数据根据当天金蝶汇率同步")
    public R<String> execute(@RequestParam(value = "queryDate", required = true)String queryDate) {
        return R.ok(reportFinanceDataService.executeAssistantAndDetailBalanceData(queryDate));
    }

    @PostMapping("/taReclassification/execute")
    @ApiOperation(value = "月末固化业务系统ta报表数据")
    public R<String> executeTa(@RequestParam(value = "queryDate", required = true)String queryDate) {
        return R.ok(reportFinanceDataService.executeTaReclassificationData(queryDate));
    }

    @PostMapping("/kingdeeRate/execute")
    @ApiOperation(value = "固化金蝶汇率")
    public R<String> executeRate(@RequestParam(value = "queryDateStart", required = true)String queryDateStart, @RequestParam(value = "queryDateEnd", required = true)String queryDateEnd) {
        return R.ok(reportFinanceDataService.executeKingdeeRateData(queryDateStart, queryDateEnd));
    }

    @PostMapping("/contractBalanceMonth/execute")
    @ApiOperation(value = "固化合同余额month")
    public R<String> executeMonth(@RequestParam("periodCodes")String periodCodes, @RequestParam("orgId") String orgIds) {
        if ("ALL".equalsIgnoreCase(orgIds)){
            orgIds = null;
        }
        return R.ok(reportFinanceDataService.executeContractMonthData(periodCodes, orgIds));
    }

    @PostMapping("/kingdee/periodClose")
    @ApiOperation(value = "执行金蝶关账处理")
    public R<String> executePeriodClose() {
        reportFinanceDataService.compareOrgPeriod();
        return R.ok("success");
    }

    @PostMapping("/kingdee/syncAssistBalancePeriodClose")
    @ApiOperation(value = "执行金蝶关账时辅助项目余额表同步")
    public R<String> executePeriodClose(@RequestParam(value = "periodCode", required = true)Integer periodCode) {
//        LocalDateTime currentMonth = LocalDateTimeUtil.parse(periodCode+"", "yyyyMM");
        accountAssistBalanceService.syncAssistBalancePeriodClose(periodCode);
        return R.ok("success");
    }
}
