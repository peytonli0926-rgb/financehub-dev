package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.finance.service.IContractMonthService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ContractMonthDataSyncJobHandler {

    @Resource
    private IContractMonthService contractMonthService;


    /**
     * 合同基本信息同步
     */
    @XxlJob(value = "contractMonthDataSyncJob")
    public void contractMonthDataSyncJob() {
        contractMonthService.dataSync();
    }
}
