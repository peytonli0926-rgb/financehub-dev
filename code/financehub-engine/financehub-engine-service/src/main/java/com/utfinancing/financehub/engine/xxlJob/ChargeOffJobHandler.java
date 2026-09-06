package com.utfinancing.financehub.engine.xxlJob;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.api.HthxFundCoreService;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.finance.service.IChargeOffService;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.service.IHthxCommonService;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChargeOffJobHandler {

    @Resource
    private IChargeOffService iChargeOffService;

    @Resource
    private IHthxCommonService hthxCommonService;

    @XxlJob("generateChargeOffSummary")
    public void generateChargeOffSummary(){
        //From：modify by zhangli.chen for 加入节假日判断逻辑 on 20250224
        if(!hthxCommonService.checkTaskExecutionDate()){
            log.info("==>>ChargeOffJobHandler.generateChargeOffSummary==>>{}", ResultEnum.CHECK_NO_PASS.getMessage());
            return ;
        }
        //End：modify by zhangli.chen for 加入节假日判断逻辑 on 20250224
        String paramDate = XxlJobHelper.getJobParam();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date voucherDate = DateUtil.offsetMonth(DateUtil.date(), -1);
        if (StringUtils.isNotEmpty(paramDate)) {
            try {
                voucherDate = formatter.parse(paramDate);
            } catch (ParseException e) {
                throw new ServiceException("参数解析失败，失败原因："+e.getMessage());
            }
        }
        int periodCode = PeriodCodeUtil.periodCodeByDate(voucherDate);
        log.info("定时任务生成chargeOff汇总报表数据开始");
        //按照金蝶关账时间生成数据，和按月余额表逻辑一致，可以通过校验一下eg_contract_balance_month是否在这个期间生成了数据，生成了代表可以生成报表数据
//        boolean flag = iChargeOffService.isGenerateBalanceMonth(periodCode);
//        if (!flag) {
//            log.info("eg_contract_balance_month 还未生成数据");
//            return;
//        }
        iChargeOffService.summaryReportByPeriodCode(periodCode);
        log.info("定时任务生成chargeOff汇总报表数据结束");
    }
}
