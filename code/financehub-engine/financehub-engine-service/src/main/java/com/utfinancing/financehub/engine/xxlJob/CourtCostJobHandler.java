package com.utfinancing.financehub.engine.xxlJob;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderJobHandlerService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.xxlJob.CourtCostJobHandler</li>
 * <li>CreateTime : 2023/11/24 12:17</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class CourtCostJobHandler {

    @Resource
    private IClaimOrderJobHandlerService iClaimOrderJobHandlerService;

    /**
     * 每半个小时执行一次
     * 魔方过来的诉讼费支付生成凭证，类型为公告费+诉讼费保全费用
     */
    @XxlJob(value = "courtCostGenerateVoucher")
    public void courtCostGenerateVoucher() {
        XxlJobHelper.log("魔方过来的诉讼费支付生成凭证");
        iClaimOrderJobHandlerService.courtCostGenerateVoucher(new ClaimOrderQueryDTO());
        XxlJobHelper.log("魔方过来的诉讼费支付生成凭证");
    }

}
