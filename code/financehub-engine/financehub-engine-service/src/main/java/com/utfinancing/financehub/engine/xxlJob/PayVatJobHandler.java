package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.finance.service.IPayVatService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class PayVatJobHandler {

    @Resource
    private IPayVatService iPayVatService;
    /**
     * 获取合同数据放入应交增值税表
     */
    @XxlJob(value = "payVatGetContract")
    public void payVatGetContract() {
        LocalDateTime startDate = LocalDateTime.now();
        printLog("获取合同数据放入应交增值税表开始，{}",startDate);
//        String msg = iPayVatService.payVatGetContract(period);
//        printLog(msg);
        LocalDateTime endDate = LocalDateTime.now();
        printLog("获取合同数据放入应交增值税表结束，{}",endDate);
        printLog("获取合同数据放入应交增值税表开始时间：{}，结束时间:{},共用时：{}秒", startDate,endDate, ChronoUnit.SECONDS.between(startDate,endDate));
    }

    private void printLog(String appendLogPattern, Object... appendLogArguments) {
        XxlJobHelper.log(appendLogPattern, appendLogArguments);
        log.info(appendLogPattern, appendLogArguments);
    }
}
