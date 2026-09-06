package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.finance.service.IKingdeeHybVoucherService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;

@Slf4j
@Component
public class KingdeeVoucherJobHandler {

    @Resource
    private IKingdeeHybVoucherService iKingdeeHybVoucherService;

    @XxlJob(value = "generateKingdeeVoucher")
    public void generateKingdeeVoucher() {
        log.info("定时任务金蝶同步恒运宝开始");
        iKingdeeHybVoucherService.generateKingdeeHybVoucher();
        log.info("定时任务金蝶同步恒运宝结束");
    }

    @XxlJob(value = "generateKingdeeMiddleVoucher")
    public void generateKingdeeMiddleVoucher() {
        log.info("定时任务金蝶同步贵安，现代物流开始");
        iKingdeeHybVoucherService.generateKingdeeMiddleVoucher();
        log.info("定时任务金蝶同步贵安，现代物流结束");
    }
}
