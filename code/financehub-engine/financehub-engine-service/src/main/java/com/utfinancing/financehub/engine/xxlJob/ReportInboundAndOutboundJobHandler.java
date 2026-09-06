package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountDetailRecordService;
import com.utfinancing.financehub.engine.finance.service.IReportFinanceInOutService;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.service.IHthxCommonService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.xxlJob.CheckAccountAndDetailJobHandler</li>
 * <li>CreateTime : 2024/03/22 12:17</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author jnc
 * @since 1.0.0
 */
@Slf4j
@Component
public class ReportInboundAndOutboundJobHandler {

    @Resource
    private IReportFinanceInOutService reportFinanceInOutService;

    @Resource
    private IHthxCommonService hthxCommonService;

    /**
     * 每天凌晨执行一次
     * 执行当月的科目余额与明细余额对账
     */
    @XxlJob(value = "ReportInboundAndOutboundJob")
    public void executeSyncData() {
        //From：modify by zhangli.chen for 加入节假日判断逻辑 on 20250224
        if(!hthxCommonService.checkTaskExecutionDate()){
            log.info("==>>ReportInboundAndOutboundJobHandler.ReportInboundAndOutboundJob==>>{}", ResultEnum.CHECK_NO_PASS.getMessage());
            return ;
        }
        //End：modify by zhangli.chen for 加入节假日判断逻辑 on 20250224
        log.info("月末执行财务入库出库表固化 开始");
        reportFinanceInOutService.syncFinanceInboundOutboundData();
        log.info("月末执行财务入库出库表固化 结束");
    }

}
