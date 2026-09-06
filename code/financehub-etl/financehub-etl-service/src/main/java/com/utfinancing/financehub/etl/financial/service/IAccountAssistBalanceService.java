package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempClosePeriodEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-04
 * @Description : AccountAssistBalance服务类接口
 * @Modified :
 */
public interface IAccountAssistBalanceService extends IService<AccountAssistBalanceEntity> {


    void removeAssistBalanceCurrentMonth(Integer periodCode);

    List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceCurrentMonth(Integer periodCode, Integer prevPeriodCode);

    List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceNextMonth(Integer periodCode, Integer nextPeriodCode);

    void syncAssistBalancePeriodClose(Integer periodCode);

    void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceEntity> entityList, String percent, Integer periodCode);

    Map<String, Long> getNextClosePeriodSeqVal();

    void setClosePeriodSeqVal(Long nextVal);
}
