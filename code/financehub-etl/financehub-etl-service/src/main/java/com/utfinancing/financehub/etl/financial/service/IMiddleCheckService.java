package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.etl.financial.entity.CheckAccountMiddleTmpEntity;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
public interface IMiddleCheckService {

    String saveMiddleDataToTmp(String periodCode);

    void batchSaveCheckAccountMiddleTmp(CountDownLatch countDownLatch, List<CheckAccountMiddleTmpEntity> entityList, String s, String periodCode);
}
