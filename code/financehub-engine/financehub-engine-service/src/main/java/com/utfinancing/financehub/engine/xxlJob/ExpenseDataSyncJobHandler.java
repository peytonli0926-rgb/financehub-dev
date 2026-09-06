package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.claim.service.IExpenseFileService;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ExpenseDataSyncJobHandler {

    @Resource
    private IExpenseFileService expenseFileService;


    /**
     * 费用数据同步
     */
    @XxlJob(value = "expenseDataSyncJob")
    public void expenseDataSyncJob() {
        try {
            expenseFileService.expenseTypeDataSync();
        } catch (Exception e) {
            log.error("费用数据同步失败!", e);
            throw new RuntimeException(e);
        }
    }
}
