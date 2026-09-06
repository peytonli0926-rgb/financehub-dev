package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NonConfirmCollectionAccountCheckingJobHandler {
    @Resource
    private IFundBusinessSystemEbankMappingService fundBusinessSystemEbankMappingService;


    /**
     * 未确认收款业务系统数据和资金系统数据对账任务
     */
    @XxlJob(value = "accountCheckingWithBusinessJob")
    public void AccountCheckingWithBusinessJob() {
        fundBusinessSystemEbankMappingService.selectNonConfirmCollectionFromFundSystem();
    }
}
