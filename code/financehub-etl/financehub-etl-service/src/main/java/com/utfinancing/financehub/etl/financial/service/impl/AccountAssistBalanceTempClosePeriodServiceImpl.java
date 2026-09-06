package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempClosePeriodEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountAssistBalanceMapper;
import com.utfinancing.financehub.etl.financial.mapper.AccountAssistBalanceTempClosePeriodMapper;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceService;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceTempClosePeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
public class AccountAssistBalanceTempClosePeriodServiceImpl extends ServiceImpl<AccountAssistBalanceTempClosePeriodMapper, AccountAssistBalanceTempClosePeriodEntity> implements IAccountAssistBalanceTempClosePeriodService {

    private final AccountAssistBalanceTempClosePeriodMapper accountAssistBalanceTempClosePeriodMapper;


    @Override
    @Async
    public void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceTempClosePeriodEntity> entityList, String percent, Integer periodCode) {
        this.saveBatch(entityList);
        log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp_close_period 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }

    @Override
    public void syncIntoAccountAssist(Integer periodCode) {
        accountAssistBalanceTempClosePeriodMapper.syncIntoAccountAssist(periodCode);
    }

    @Override
    public void clearData() {
        accountAssistBalanceTempClosePeriodMapper.clearData();
    }
}

