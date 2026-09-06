package com.utfinancing.financehub.etl.xxljob;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.service.INonConfirmAmountForBusinessService;
import com.utfinancing.financehub.etl.financial.service.InvoiceService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class NonConfirmAmountJobHandler {

    @Resource
    private INonConfirmAmountForBusinessService nonConfirmAmountForBusinessService;

    /**
     * 业务系统同步未确认收款
     */
    @XxlJob(value = "nonConfirmAmountSync")
    public void nonConfirmAmountSync() {
        nonConfirmAmountForBusinessService.nonConfirmAmountSyncJob();
    }
}
