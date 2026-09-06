package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempClosePeriodEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempEntity;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-04
 * @Description : AccountAssistBalance服务类接口
 * @Modified :
 */
public interface IAccountAssistBalanceTempClosePeriodService extends IService<AccountAssistBalanceTempClosePeriodEntity> {


    void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceTempClosePeriodEntity> entityList, String percent, Integer periodCode);

    void syncIntoAccountAssist(Integer periodCode);

    void clearData();
}
