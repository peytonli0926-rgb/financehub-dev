package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataResultEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckSqlEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-29
 * @Description : CheckSql服务类接口
 * @Modified :
 */
public interface ICheckSqlService extends IService<CheckSqlEntity> {

    String saveCommonToTmp(Integer periodCode, String sqlMark);

    void batchSaveToTmp(CountDownLatch countDownLatch, List<CheckCommonDataEntity> entityList, String percent, Integer periodCode,  String systemCode, String sqlMark);

    void batchSaveToResultTmp(CountDownLatch countDownLatch, List<CheckCommonDataResultEntity> entityList, String percent, Integer periodCode, String systemCode, String sqlMark);
}
