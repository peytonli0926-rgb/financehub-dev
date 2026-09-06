package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.ContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.etl.financial.mapper.ContractBalanceLatestMapper;
import com.utfinancing.financehub.etl.financial.service.IContractBalanceLatestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-02
 * @Description :  ContractBalanceLatest服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractBalanceLatestServiceImpl extends ServiceImpl<ContractBalanceLatestMapper, ContractBalanceLatestEntity> implements IContractBalanceLatestService {

    private final ContractBalanceLatestMapper contractBalanceLatestMapper;

    /**
     * 查询最新的合同科目余额, 只返回余额字段
     *
     * @param businessCode 业务编码， 必须要传
     * @param contractCode 合同编码， 如果不传，查询整体
     * @param clientCode   客户编码， 如果不传，查询整体
     * @return
     */
    @Override
    public Map<String, Object> getLastBalanceMap(String businessCode, String orgId, String clientCode, String contractCode, String billContractCode) {
        LambdaQueryWrapper<ContractBalanceLatestEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceLatestEntity>lambdaQuery();
//        lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getBusinessCode, businessCode);
        if (StrUtil.isNotBlank(orgId)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getOrgId, orgId);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getOrgId);
        }
        if (StrUtil.isNotBlank(clientCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getClientCode, clientCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(contractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getContractCode, contractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getContractCode);
        }
        if (StrUtil.isNotBlank(billContractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getBillContractCode, billContractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getBillContractCode);
        }
        lambdaQueryWrapper.last("limit 1");
        Map<String, Object> rowMap = this.getMap(lambdaQueryWrapper);
        if (MapUtil.isEmpty(rowMap)) {
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put("business_code", businessCode);
            emptyMap.put("client_code", clientCode);
            emptyMap.put("contract_code", contractCode);
            emptyMap.put("org_id", orgId);
            emptyMap.put("bill_contract_code", billContractCode);
            return emptyMap;
        }
        //过滤发生额和其他字段，只保留余额字段
        for (Iterator<Map.Entry<String, Object>> it = rowMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> item = it.next();
            if (!StrUtil.containsAny(item.getKey(), "_balance") && !StrUtil.equals(item.getKey(), "id")) {
                it.remove();
            }
        }
        rowMap.put("business_code", businessCode);
        rowMap.put("client_code", clientCode);
        rowMap.put("contract_code", contractCode);
        rowMap.put("org_id", orgId);
        rowMap.put("bill_contract_code", billContractCode);
        return rowMap;
    }

    @Override
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
        contractBalanceLatestMapper.insertContractBalance(columns, values);
    }

    @Override
    public void updateLastBalanceMap(Map<String, Object> rowMap) {
        Long id = (Long) rowMap.get("id");
        rowMap.put("update_time", LocalDateTime.now());
        rowMap.put("create_time", LocalDateTime.now());
        rowMap.remove("id");
        contractBalanceLatestMapper.updateContractBalance(rowMap, id);
    }

}

