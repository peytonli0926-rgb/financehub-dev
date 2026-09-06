package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFieldEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckCommonFinanceDataVO;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckCommonFinanceDataMapper;
import com.utfinancing.financehub.engine.finance.service.ICheckCommonFinanceDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description :  CheckCommonFinanceData服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckCommonFinanceDataServiceImpl extends ServiceImpl<CheckCommonFinanceDataMapper, CheckCommonFinanceDataEntity> implements ICheckCommonFinanceDataService {

    private final CheckCommonFinanceDataMapper checkCommonFinanceDataMapper;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return checkCommonFinanceDataMapper.selectDataCommon(querySql);
    }

//    @Async
    @Override
    public void batchSaveToTmp(CountDownLatch countDownLatch, List<CheckCommonFinanceDataEntity> entityList, String percent, String periodCode, String systemCode, String sqlMark) {
        this.saveBatch(entityList);
        log.info("同步其他业务系统数据数据保存到原始表 单页保存完成.dbCode:{} sqlMark:{} periodCode:{}, 完成比例:{}", systemCode, sqlMark, periodCode, percent);
//        countDownLatch.countDown();
    }

    @Override
    public void deleteCheckCommonData(String businessType) {
        checkCommonFinanceDataMapper.deleteCheckCommonData(businessType);
    }

    @Override
    public void deleteCheckCommonDataByExecuteDate(Integer executeDateCode, String businessType, Integer periodCode) {
        checkCommonFinanceDataMapper.deleteCheckCommonDataByExecuteDate(executeDateCode, businessType, periodCode);
    }

}

