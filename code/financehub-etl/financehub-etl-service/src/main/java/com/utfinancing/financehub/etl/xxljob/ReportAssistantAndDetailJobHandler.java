package com.utfinancing.financehub.etl.xxljob;

import com.utfinancing.financehub.etl.financial.service.IReportFinanceDataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.xxljob.InvoiceClaimJobHandler</li>
 * <li>CreateTime : 2023/11/15 16:39</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
@Component
public class ReportAssistantAndDetailJobHandler {

    @Resource
    private IReportFinanceDataService reportFinanceDataService;

    /**
     * 报表模块-租赁大表数据处理
     * 每天凌晨一次
     */
    @XxlJob(value = "syncAssistantAndDetailJob")
    public void syncAssistantAndDetailJob() {
        String queryDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        XxlJobHelper.log("同步金蝶汇率 开始");
        reportFinanceDataService.syncKingdeeExchangeRate(queryDate);
        XxlJobHelper.log("同步金蝶汇率 结束");

        XxlJobHelper.log("同步辅助余额帐 开始");
        reportFinanceDataService.syncAssistantBalance(queryDate);
        XxlJobHelper.log("同步辅助余额帐 结束");

        XxlJobHelper.log("同步明细余额帐 开始");
        reportFinanceDataService.syncDetailBalance(queryDate);
        XxlJobHelper.log("同步明细余额帐 结束");
    }

}
