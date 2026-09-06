package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderJobHandlerService;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountDetailRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
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
@Component
public class CheckAccountAndDetailJobHandler {

    @Resource
    private ICheckAccountDetailRecordService iCheckAccountDetailRecordService;

    /**
     * 每天凌晨执行一次
     * 执行当月的科目余额与明细余额对账
     */
    @XxlJob(value = "CheckAccountAndDetailDailyJob")
    public void CheckAccountAndDetail() {
        XxlJobHelper.log("执行当月的科目余额与明细余额对账 开始");
        iCheckAccountDetailRecordService.checkDetail(Integer.valueOf(DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMM")), CheckTypeEnum.JOB.getCode());
        XxlJobHelper.log("执行当月的科目余额与明细余额对账 结束");
    }

}
