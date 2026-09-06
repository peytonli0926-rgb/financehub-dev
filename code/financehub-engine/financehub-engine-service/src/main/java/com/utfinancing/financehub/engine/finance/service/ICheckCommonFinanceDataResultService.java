package com.utfinancing.financehub.engine.finance.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataResultQueryDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description : CheckCommonFinanceDataResult服务类接口
 * @Modified :
 */
public interface ICheckCommonFinanceDataResultService extends IService<CheckCommonFinanceDataResultEntity> {

    void batchSaveToResultTmp(CountDownLatch countDownLatch, List<CheckCommonFinanceDataResultEntity> entityList, String s, String periodCode, String systemCode, String sqlMark);

    IPage<Map<String, Object>> selectCommonPage(CheckCommonFinanceDataResultQueryDTO queryDTO);

    void deleteCheckCommonData(String businessType);

    void deleteCheckCommonDataByExecuteDate(Integer executeDateCode, String businessType, Integer periodCode);
}
