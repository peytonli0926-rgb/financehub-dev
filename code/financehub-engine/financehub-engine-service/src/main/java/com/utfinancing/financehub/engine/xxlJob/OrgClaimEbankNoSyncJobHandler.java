package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.engine.finance.entity.OrgFundClaimJobRecordEntity;
import com.utfinancing.financehub.engine.finance.service.IOrgClaimEbankNoService;
import com.utfinancing.financehub.engine.finance.service.IOrgFundClaimJobRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class OrgClaimEbankNoSyncJobHandler {

    @Resource
    private IOrgClaimEbankNoService orgClaimEbankNoService;

    @Resource
    private  IOrgFundClaimJobRecordService orgFundClaimJobRecordService;

    /**
     * 同步机构认领的网银编号
     */
    @XxlJob(value = "orgClaimEbankNoSyncJob")
    public void orgClaimEbankNoSyncJob() {
        log.info("同步机构认领的网银编号, 任务启动");
        String paramStr = XxlJobHelper.getJobParam();
        log.info("同步机构认领的网银编号==>>输入任务参数:{}",paramStr);
        if (StringUtils.isBlank(paramStr)) {
            OrgFundClaimJobRecordEntity lastJobRecord = orgFundClaimJobRecordService.getLastJobRecord();
            if(lastJobRecord!=null && StringUtils.isNotEmpty(lastJobRecord.getLastQueryDate())){
                paramStr = DateUtil.formatDate(DateUtils.addDays(DateUtils.parseDate(lastJobRecord.getLastQueryDate()), 1));
            }else{
                // 默认查询前一天的数据
                paramStr = DateUtil.formatDate(DateUtils.addDays(new Date(), -1));
            }
        }
        log.info("同步机构认领的网银编号==>>最终查询日期:{}",paramStr);
        try {
            Map<String, Object> queryResult =  orgClaimEbankNoService.orgClaimEbankNoSync(DateUtils.parseDate(paramStr));
            orgFundClaimJobRecordService.saveFundClaimJobRecord(queryResult,paramStr);
        }catch (Exception e){
            log.error("{}:同步机构认领的网银编号报错：{}",paramStr,e.getMessage());
        }
        log.info("同步机构认领的网银编号, 任务结束");
    }
}
