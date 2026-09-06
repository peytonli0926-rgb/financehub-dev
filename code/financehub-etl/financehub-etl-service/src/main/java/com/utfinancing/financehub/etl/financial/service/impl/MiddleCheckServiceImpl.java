package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountKingdeeTmpEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountMiddleTmpEntity;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeCheckDataService;
import com.utfinancing.financehub.etl.middle.service.IMiddleCheckDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@RequiredArgsConstructor
@Service
@Slf4j
//@Transactional
public class MiddleCheckServiceImpl implements IMiddleCheckService {

//    @Resource
    private final ICheckAccountMiddleTmpService checkAccountMiddleTmpService;

    private final IMiddleCheckDataService middleCheckDataService;

    @Override
    public String saveMiddleDataToTmp(String periodCode) {
        List<Map<String, Object>> list = middleCheckDataService.getMiddleCheckData(periodCode);
        if(CollectionUtil.isEmpty(list)){
//            throw new ServiceException("未找到periodCode"+periodCode+" 内的对账数据");
            return "未找到periodCode"+periodCode+" 内的对账数据";
        }

        checkAccountMiddleTmpService.clearTableData();
        Integer period = Integer.valueOf(periodCode);
        List<CheckAccountMiddleTmpEntity> tmpEntityList = Lists.newArrayList();
        for(int i=0; i<list.size();i++){
            Map<String, Object> map = list.get(i);
            CheckAccountMiddleTmpEntity tmpEntity = new CheckAccountMiddleTmpEntity();
            tmpEntity.setPeriodCode(period);
            tmpEntity.setCurrencyType(getString(map.get("CURRENCY_CODE")));
            tmpEntity.setAccountCode(getString(map.get("ACCOUNT_CODE")));
            tmpEntity.setOrgId(getString(map.get("ORG_ID")));
            tmpEntity.setBankAccount(getString(map.get("BANK_NUMBER")));
            tmpEntity.setBillContractCode(getString(map.get("BATCH_NUMBER")));
            tmpEntity.setClientCode(getString(map.get("CLIENT_CODE")));
            tmpEntity.setContractCode(getString(map.get("CONTRACT_CODE")));
            tmpEntity.setCreditAmount(getBigDecimal(map.get("CREDIT_AMOUNT")));
            tmpEntity.setDebitAmount(getBigDecimal(map.get("DEBIT_AMOUNT")));
            tmpEntity.setBusinessDate(getString(map.get("BUSINESS_DATE")));
            tmpEntity.setVoucherDate(getString(map.get("VOUCHER_DATE")));
            tmpEntity.setSystemCode(getString(map.get("SYSTEM_CODE")));
            tmpEntityList.add(tmpEntity);
        }

        log.info("同步金蝶中间表科目发生额数据开始保存入库  periodCode:{}  转换后数据量:{}", periodCode, tmpEntityList.size());
        List<List<CheckAccountMiddleTmpEntity>> entityPage = ListUtil.partition(tmpEntityList, 1000);
        IMiddleCheckService middleCheckService = SpringUtils.getBean(IMiddleCheckService.class);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        log.info("同步金蝶中间表科目发生额数据 分组完成  periodCode:{}, pageCount:{}", periodCode, pageCount);
        int i = 1;
        for (List<CheckAccountMiddleTmpEntity> entityList: entityPage){
            middleCheckService.batchSaveCheckAccountMiddleTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount).toString()),2), periodCode);
            i++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("同步金蝶中间表科目发生额数据完成保存入库  periodCode:{}  转换后数据量:{}", periodCode, tmpEntityList.size());
        return "success";
    }

    @Async
    @Override
    public void batchSaveCheckAccountMiddleTmp(CountDownLatch countDownLatch, List<CheckAccountMiddleTmpEntity> entityList, String percent, String periodCode) {
        checkAccountMiddleTmpService.saveBatch(entityList);
        log.info("同步金蝶中间表科目发生额数据 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }


    private static String getString(Object column) {
        return ObjectUtil.isEmpty(column) ? null : column.toString();
    }

    private static BigDecimal getBigDecimal(Object column) {
        return ObjectUtil.isEmpty(column) ? BigDecimal.ZERO : (BigDecimal) column;
    }
}
