package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountDetailContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountDetailContractBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-05-08
 * @Description :  CheckAccountDetailContractBalance服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountDetailContractBalanceServiceImpl extends ServiceImpl<CheckAccountDetailContractBalanceMapper, CheckAccountDetailContractBalanceEntity> implements ICheckAccountDetailContractBalanceService {

    private final CheckAccountDetailContractBalanceMapper checkAccountDetailContractBalanceMapper;

    @Override
    public List<CheckAccountDetailContractBalanceEntity> selectContractTmpTableData(Integer periodCode) {
        return checkAccountDetailContractBalanceMapper.selectContractTmpTableData(periodCode);
    }

    @Override
    public void saveTmpBatch(List<CheckAccountDetailContractBalanceEntity> list, Integer periodCode) {
        log.info("将账期下合同表的最新数据 保存到临时表 periodCode:{}  转换后数据量:{}", periodCode, list.size());
        List<List<CheckAccountDetailContractBalanceEntity>> entityPage = ListUtil.partition(list, 5000);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        log.info("将账期下合同表的最新数据 保存到临时表 分组完成  periodCode:{}, pageCount:{}", periodCode, pageCount);
        int i = 1;
        for (List<CheckAccountDetailContractBalanceEntity> entityList: entityPage){
            this.batchSaveToTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode);
            i++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("将账期下合同表的最新数据，保存到临时表完成 入库 periodCode:{}  转换后数据量:{}", periodCode, list.size());

    }

    private void batchSaveToTmp(CountDownLatch countDownLatch, List<CheckAccountDetailContractBalanceEntity> entityList, String percent, Integer periodCode) {
        this.saveBatch(entityList);
        log.info("将账期下合同表的最新数据保存到临时表 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }
}

