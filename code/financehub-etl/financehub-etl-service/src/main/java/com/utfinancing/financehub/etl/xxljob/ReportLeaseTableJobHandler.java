package com.utfinancing.financehub.etl.xxljob;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.service.IReportFinanceDataService;
import com.utfinancing.financehub.etl.financial.service.InvoiceService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class ReportLeaseTableJobHandler {

    @Resource
    private IReportFinanceDataService reportFinanceDataService;

    /**
     * 报表模块-租赁大表数据处理
     * 每天凌晨一次
     */
    @XxlJob(value = "syncReportLeaseTableJob")
    public void syncReportLeaseTableJob() {
//        XxlJobHelper.log("财务中台合同表report_flag标志刷新开始");
//        reportFinanceDataService.fillContractReportFlag();
//        XxlJobHelper.log("财务中台合同表report_flag标志刷新结束");

        XxlJobHelper.log("财务中台与金蝶账期比较开始");
        reportFinanceDataService.compareOrgPeriod();
        XxlJobHelper.log("财务中台与金蝶账期比较结束");
    }

}
