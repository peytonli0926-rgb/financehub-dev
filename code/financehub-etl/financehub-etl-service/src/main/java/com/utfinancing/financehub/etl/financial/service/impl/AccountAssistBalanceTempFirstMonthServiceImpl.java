package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempFirstMonthEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountAssistBalanceMapper;
import com.utfinancing.financehub.etl.financial.mapper.AccountAssistBalanceTempFirstMonthMapper;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceService;
import com.utfinancing.financehub.etl.financial.service.IAccountAssistBalanceTempFirstMonthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AccountAssistBalanceTempFirstMonthServiceImpl extends ServiceImpl<AccountAssistBalanceTempFirstMonthMapper, AccountAssistBalanceTempFirstMonthEntity> implements IAccountAssistBalanceTempFirstMonthService {

    private final AccountAssistBalanceTempFirstMonthMapper accountAssistBalanceTempFirstMonthMapper;


    @Override
    @Async
    public void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceTempFirstMonthEntity> entityList, String percent, Integer periodCode) {
        this.saveBatch(entityList);
        log.info("同步金蝶辅助帐余额表 同步到eg_account_assist_balance_temp_fist_month 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }

    @Override
    public void deleteTable() {
        accountAssistBalanceTempFirstMonthMapper.deleteTable();
    }
}

