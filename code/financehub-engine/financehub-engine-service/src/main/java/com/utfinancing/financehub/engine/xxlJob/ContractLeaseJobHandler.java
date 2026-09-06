package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ContractLeaseJobHandler {

    @Resource
    private IRepaymentPlanService repaymentPlanService;


    /**
     * 抽取过去一年内起租的合同，进行偿还计划生成
     */
    @XxlJob(value = "contractOnHireJob")
    public void contractOnHireJob() {
        XxlJobHelper.log("抽取过去一年内起租的合同，进行偿还计划生成 Start...");
        repaymentPlanService.contractOnHireTask();
        XxlJobHelper.log("抽取过去一年内起租的合同，进行偿还计划生成 End");
    }
}
