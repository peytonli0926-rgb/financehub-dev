package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.CheckCommonFieldEntity;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description : CheckCommonFinanceData服务类接口
 * @Modified :
 */
public interface ICheckCommonFinanceDataService extends IService<CheckCommonFinanceDataEntity> {
    List<Map<String, Object>> selectDataCommon(String querySql);

    void batchSaveToTmp(CountDownLatch countDownLatch, List<CheckCommonFinanceDataEntity> entityList, String s, String periodCode, String systemCode, String sqlMark);

    void deleteCheckCommonData(String businessType);

    void deleteCheckCommonDataByExecuteDate(Integer executeDateCode, String businessType, Integer periodCode);
}
