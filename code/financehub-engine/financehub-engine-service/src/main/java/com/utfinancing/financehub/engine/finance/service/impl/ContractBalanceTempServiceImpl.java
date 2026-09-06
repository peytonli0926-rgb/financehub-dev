package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.engine.enums.ContractBalanceColumnsEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceTempEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceTempMapper;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceTempService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-02-05
 * @Description :  ContractBalanceTemp服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractBalanceTempServiceImpl extends ServiceImpl<ContractBalanceTempMapper, ContractBalanceTempEntity>
        implements IContractBalanceTempService {

    private final ContractBalanceTempMapper contractBalanceTempMapper;


    public void insertMap(Map<String, Object> rowMap) {
        rowMap.put("id", IdWorker.getId());
        rowMap.put("update_time", LocalDateTime.now());
        rowMap.put("create_time", LocalDateTime.now());
        List<String> columns = ListUtil.toList();
        List<Object> values = ListUtil.toList();
        for (Map.Entry<String, Object> mapEntry : rowMap.entrySet()) {
            columns.add(mapEntry.getKey());
            values.add(mapEntry.getValue());
        }
        //入库
        contractBalanceTempMapper.insertContractBalanceTemp(columns, values);
    }

    /**
     * 根据凭证id删除余额临时表数据
     */
    public int delBalanceTempByVoucherId(List<Long> idList) {
        LambdaQueryWrapper<ContractBalanceTempEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(ContractBalanceTempEntity::getVoucherId, idList);
        return contractBalanceTempMapper.delete(wrapper);
    }

    /**
     * 删除临时表的发生额数据
     */
    @Async
    public void delBalanceTeamData(Map<String, Object> rowMap) {
        LambdaQueryWrapper<ContractBalanceTempEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ContractBalanceTempEntity::getContractCode,
                rowMap.get(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode()));
        if (rowMap.get(ContractBalanceColumnsEnum.CLIENT_CODE.getCode()) != null) {
            wrapper.eq(ContractBalanceTempEntity::getClientCode,
                    rowMap.get(ContractBalanceColumnsEnum.CLIENT_CODE.getCode()));
        }
        wrapper.eq(ContractBalanceTempEntity::getBusinessCode,
                rowMap.get(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode()));
        contractBalanceTempMapper.delete(wrapper);
    }
}

