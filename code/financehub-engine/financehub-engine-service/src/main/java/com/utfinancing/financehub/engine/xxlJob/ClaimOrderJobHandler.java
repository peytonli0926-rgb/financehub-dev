package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderInvoiceEntity;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderSpecialEntity;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVoucherVO;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderInvoiceService;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderJobHandlerService;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import com.utfinancing.financehub.engine.enums.ClaimOrderSpecialExpenseTypeEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.xxlJob.ClaimOrderJobHandler</li>
 * <li>CreateTime : 2023/11/22 13:45</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Component
public class ClaimOrderJobHandler {

    @Resource
    private IClaimOrderJobHandlerService iClaimOrderJobHandlerService;

    /**
     * 每隔半个小时执行一次
     * 成本类GPS费用包括：设备费，安装费，服务费，
     * 成本类手环费用包括：设备费，安装费，服务费
     * 成本类-收车费、抵押费、解抵押费
     */
    @XxlJob(value = "costGenerateVoucher")
    public void costGenerateVoucher() {
        XxlJobHelper.log("成本类相关费用生成凭证开始");
        //查询费用类型为：GPS费用并且是未生成凭证的数据 状态为付款完成的生成付款凭证，状态为审批完成的且发票表里有数据的生成收票凭证
        iClaimOrderJobHandlerService.costGenerateVoucher(new ClaimOrderSpecialQueryDTO());
        XxlJobHelper.log("成本类相关费用生成凭证结束");
    }
}
