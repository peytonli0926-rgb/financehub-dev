package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class BusinessClaimPaymentJobHandler {

    @Resource
    private IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;


    /**
     * 定时抽取业务系统已经认领的收款记录
     */
    @XxlJob(value = "businessClaimPaymentJob")
    public void businessClaimPaymentJob() {
        //查询费用类型为：GPS费用并且是未生成凭证的数据 状态为付款完成的生成付款凭证，状态为审批完成的且发票表里有数据的生成收票凭证
        businessClaimRepaymentRecordService.extractClaimPaymentRecord();
    }
}
