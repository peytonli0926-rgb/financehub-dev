package com.utfinancing.financehub.engine.xxlJob;

import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ContractBasicDataSyncJobHandler {

    @Resource
    private IContractService contractService;


    /**
     * 合同基本信息同步
     */
    @XxlJob(value = "contractBasicDataSyncJob")
    public void contractBasicDataSyncJob() {
        String jobParam = XxlJobHelper.getJobParam();
        String leaseDateStart = JSONObject.parseObject(jobParam).getString("leaseDateStart");
        //查询费用类型为：GPS费用并且是未生成凭证的数据 状态为付款完成的生成付款凭证，状态为审批完成的且发票表里有数据的生成收票凭证
        contractService.basicDataSync(leaseDateStart);
    }

    /**
     * 带-A的合同除了id、合同、签约主体、合同状态、创建人、创建时间、更新人、更新时间、财务合同状态、财务合同状态更新时间以外的其他字段均需要复制不带-A合同的原数据，
     * 例如KCE20231890-A需要复制KCE20231890的合同信息（除了上述字段）
     */
    @XxlJob(value = "updateContractABasicDataSyncJob")
    public void updateContractABasicDataSyncJob() {
        contractService.updateContractABasicDataSync();
    }
}
