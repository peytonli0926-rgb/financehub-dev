package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempFirstMonthEntity;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-04
 * @Description : AccountAssistBalance服务类接口
 * @Modified :
 */
public interface IAccountAssistBalanceTempFirstMonthService extends IService<AccountAssistBalanceTempFirstMonthEntity> {


    void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceTempFirstMonthEntity> entityList, String percent, Integer periodCode);

    void deleteTable();
}
