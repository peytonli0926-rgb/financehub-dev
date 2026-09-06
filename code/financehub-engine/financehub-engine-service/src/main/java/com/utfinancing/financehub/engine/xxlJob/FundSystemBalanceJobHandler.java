package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IFundEbankTransactionDataService;
import com.utfinancing.financehub.engine.finance.service.IFundPaymentDataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.xxlJob.FundSystemBalanceJobHandler</li>
 * <li>CreateTime : 2023/12/19 10:59</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Component
public class FundSystemBalanceJobHandler {

    @Resource
    private IFundEbankTransactionDataService fundEbankTransactionDataService;
    @Resource
    private IFundPaymentDataService fundPaymentDataService;

    @XxlJob(value = "fundSystemBalanceGenerateVoucher")
    public void fundSystemBalanceGenerateVoucher(){
        //参数格式：yyyy-MM-dd
        String businessDate = XxlJobHelper.getJobParam();
//        String lastBusinessDate = XxlJobHelper.getJobParam();
        FundEbankTransactionDataQueryDTO queryDTO = new FundEbankTransactionDataQueryDTO();
        FundPaymentDataQueryDTO paymentDataQueryDTO = new FundPaymentDataQueryDTO();

        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
//            lastBusinessDate = DateUtil.format(DateUtils.addDays(DateUtils.getNowDate(), -1),"yyyy-MM-dd");
        }
        queryDTO.setBusinessDate(businessDate);
        paymentDataQueryDTO.setBusinessDate(businessDate);
        XxlJobHelper.log("开始-资金系统收付款生成凭证参数："+ JSON.toJSONString(queryDTO));
        log.info("开始-资金系统收付款生成凭证参数:"+JSON.toJSONString(queryDTO));
        fundEbankTransactionDataService.transactionGenerateVoucher(queryDTO);
        XxlJobHelper.log("结束-资金系统收付款生成凭证");
        log.info("结束-资金系统收付款生成凭证");

        XxlJobHelper.log("开始-资金系统付款生成凭证参数："+ JSON.toJSONString(paymentDataQueryDTO));
        log.info("开始-资金系统付款生成凭证参数："+ JSON.toJSONString(paymentDataQueryDTO));
        fundPaymentDataService.payMentGenerateVoucher(paymentDataQueryDTO);
        XxlJobHelper.log("结束-资金系统付款生成凭证");
        log.info("结束-资金系统付款生成凭证");
    }

    /**
     * 资金恒运映射表
     */
    @XxlJob(value = "generateMappingVoucher")
    public void generateMappingVoucher(){
        log.info("资金恒运映射表生成凭证开始");
        fundEbankTransactionDataService.generateMappingVoucher();
        log.info("资金恒运映射表生成凭证结束");
    }
}
