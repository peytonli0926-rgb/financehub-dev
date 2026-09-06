package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempClosePeriodEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempFirstMonthEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountAssistBalanceMapper;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceService;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceTempClosePeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-04
 * @Description :  AccountAssistBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AccountAssistBalanceServiceImpl extends ServiceImpl<AccountAssistBalanceMapper, AccountAssistBalanceEntity> implements IAccountAssistBalanceService {

    private final AccountAssistBalanceMapper accountAssistBalanceMapper;

    @Resource
    private IAccountAssistBalanceTempClosePeriodService accountAssistBalanceTempClosePeriodService;


    @Override
    public void removeAssistBalanceCurrentMonth(Integer periodCode) {
        accountAssistBalanceMapper.removeAssistBalanceCurrentMonth(periodCode);
    }

    @Override
    public List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceCurrentMonth(Integer periodCode, Integer prevPeriodCode) {
        accountAssistBalanceMapper.syncAssistBalanceCurrentMonth(periodCode, prevPeriodCode);
        return null;
    }

    @Override
    public List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceNextMonth(Integer periodCode, Integer nextPeriodCode) {
        accountAssistBalanceMapper.syncAssistBalanceNextMonth(periodCode, nextPeriodCode);
        return null;
    }

    @Override
    public void syncAssistBalancePeriodClose(Integer periodCode) {
        LocalDateTime currentMonth = LocalDateTimeUtil.parse(periodCode+"", "yyyyMM");

        String nextPeriodCodeStr = LocalDateTimeUtil.format(currentMonth.plusMonths(1), "yyyyMM");
        String prevPeriodCodeStr = LocalDateTimeUtil.format(currentMonth.minusMonths(1), "yyyyMM");
        log.info("同步关账辅助帐数据 设置sequence值开始");
        Map<String, Long> map = this.getNextClosePeriodSeqVal();
        this.setClosePeriodSeqVal(map.get("next_val"));
        log.info("同步关账辅助帐数据 设置sequence值结束");

        log.info("清空上月关账时生成的辅助科目余额表当月数据 period code:"+periodCode+" 开始");
        this.removeAssistBalanceCurrentMonth(periodCode);
        log.info("清空上月关账时生成的辅助科目余额表当月数据 period code:"+periodCode+" 完成");


        log.info("清空eg_account_assist_balance_temp_close_period 开始");
        accountAssistBalanceTempClosePeriodService.clearData();
        log.info("清空eg_account_assist_balance_temp_close_period 完成");

        log.info("同步凭证和手工凭证数据到辅助科目余额关账临时表 period code:"+periodCode+" 开始");
//        List<AccountAssistBalanceTempClosePeriodEntity> currentMonthList = this.syncAssistBalanceCurrentMonth(periodCode, Integer.valueOf(prevPeriodCodeStr));
        this.syncAssistBalanceCurrentMonth(periodCode, Integer.valueOf(prevPeriodCodeStr));
//        log.info("同步金蝶辅助帐余额表 本月数据 同步到eg_account_assist_balance_temp_close_period 开始保存入库  periodCode:{}, 转换后数据量:{}", periodCode, currentMonthList.size());
//        List<List<AccountAssistBalanceTempClosePeriodEntity>> entityPage = ListUtil.partition(currentMonthList, 500);
//        IAccountAssistBalanceService accountAssistBalanceService = SpringUtils.getBean(IAccountAssistBalanceService.class);
//        int pageCount = entityPage.size();
//        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
//        log.info("同步金蝶辅助帐余额表 本月数据 同步到eg_account_assist_balance_temp_close_period 分组完成  periodCode:{}, pageCount:{}", periodCode, pageCount);
//        int i = 1;
//        for (List<AccountAssistBalanceTempClosePeriodEntity> entityList: entityPage){
////            accountAssistBalanceService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
//            accountAssistBalanceTempClosePeriodService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
//            i++;
//        }
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        log.info("同步凭证和手工凭证数据到辅助科目余额关账临时表 period code:"+periodCode+" 完成");

        log.info("同步金蝶辅助帐余额表 同步关账临时表数据到eg_account_assist_balance 开始  periodCode:{}", periodCode);
        accountAssistBalanceTempClosePeriodService.syncIntoAccountAssist(periodCode);
        log.info("同步金蝶辅助帐余额表 同步关账临时表数据到eg_account_assist_balance 结束  periodCode:{}", periodCode);


        log.info("同步凭证和手工凭证数据下一月数据的期初数据到辅助科目余额关账临时表 period code:"+Integer.valueOf(nextPeriodCodeStr)+" 开始");
        this.syncAssistBalanceNextMonth(periodCode, Integer.valueOf(nextPeriodCodeStr));
//        List<AccountAssistBalanceTempClosePeriodEntity> nextMonthList = this.syncAssistBalanceNextMonth(periodCode, Integer.valueOf(nextPeriodCodeStr));
//        log.info("同步金蝶辅助帐余额表 下月数据 同步到eg_account_assist_balance_temp_close_period 开始保存入库  periodCode:{}, 转换后数据量:{}", nextPeriodCodeStr, nextMonthList.size());
//        List<List<AccountAssistBalanceTempClosePeriodEntity>> entityPageNext = ListUtil.partition(nextMonthList, 500);
////        IAccountAssistBalanceService accountAssistBalanceService2 = SpringUtils.getBean(IAccountAssistBalanceService.class);
//        int pageCountNext = entityPageNext.size();
//        CountDownLatch countDownLatchNext = new CountDownLatch(pageCountNext);
//        log.info("同步金蝶辅助帐余额表 下月数据 同步到eg_account_assist_balance_temp_close_period 分组完成  periodCode:{}, pageCount:{}", nextPeriodCodeStr, pageCountNext);
//        int i2 = 1;
//        for (List<AccountAssistBalanceTempClosePeriodEntity> entityList: entityPageNext){
////            accountAssistBalanceService2.batchSaveAccountAssistBalance(countDownLatchNext, entityList, NumberUtil.formatPercent(i2/Double.valueOf(Integer.valueOf(pageCountNext).toString()),2), Integer.valueOf(nextPeriodCodeStr));
//            accountAssistBalanceTempClosePeriodService.batchSaveAccountAssistBalance(countDownLatch, entityList, NumberUtil.formatPercent(i2/Double.valueOf(Integer.valueOf(pageCountNext).toString()),2), Integer.valueOf(nextPeriodCodeStr));
//            i2++;
//        }
//        try {
//            countDownLatchNext.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        log.info("同步凭证和手工凭证数据下一月数据的期初数据到辅助科目余额关账临时表 period code:"+Integer.valueOf(nextPeriodCodeStr)+" 完成");
//
        log.info("同步金蝶辅助帐余额表 同步关账临时表数据到eg_account_assist_balance 开始  periodCode:{}", periodCode);
        accountAssistBalanceTempClosePeriodService.syncIntoAccountAssist(Integer.valueOf(nextPeriodCodeStr));
        log.info("同步金蝶辅助帐余额表 同步关账临时表数据到eg_account_assist_balance 结束  periodCode:{}", periodCode);

    }

    @Override
    @Async
    public void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceEntity> entityList, String percent, Integer periodCode) {
        this.saveBatch(entityList);
        log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }

    @Override
    public Map<String, Long> getNextClosePeriodSeqVal() {
        return accountAssistBalanceMapper.getNextClosePeriodSeqVal();
    }

    @Override
    public void setClosePeriodSeqVal(Long nextVal) {
        accountAssistBalanceMapper.setClosePeriodSeqVal(nextVal);
    }
}

