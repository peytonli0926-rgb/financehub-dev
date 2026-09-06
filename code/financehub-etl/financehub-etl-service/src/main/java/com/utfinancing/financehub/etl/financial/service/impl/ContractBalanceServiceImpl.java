package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.model.dto.ContractBalanceQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.ContractBalanceDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.etl.financial.entity.ContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.etl.financial.service.IContractBalanceLatestService;
import com.utfinancing.financehub.etl.financial.service.IContractBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :  ContractBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractBalanceServiceImpl extends ServiceImpl<ContractBalanceMapper, ContractBalanceEntity> implements IContractBalanceService {

    private final ContractBalanceMapper contractBalanceMapper;
    private final IContractBalanceLatestService contractBalanceLatestService;

    @Override
    public Long saveContractBalance(ContractBalanceDTO dto) {
        ContractBalanceEntity entity = BeanUtil.copyProperties(dto, ContractBalanceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractBalance(Long id, ContractBalanceDTO dto) {
        ContractBalanceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractBalanceDTO getContractBalanceDTOById(Long id) {
        ContractBalanceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractBalanceDTO.class);
    }

    @Override
    public IPage<ContractBalanceVO> selectPage(ContractBalanceQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractBalanceEntity> queryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ContractBalanceEntity> entityIPage = contractBalanceMapper.selectPage(new Page<ContractBalanceEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractBalanceVO.class);
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
        contractBalanceMapper.insertContractBalance(columns, values);
    }

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
        LambdaQueryWrapper<ContractBalanceEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
//        lambdaQueryWrapper.eq(ContractBalanceEntity::getBusinessCode, businessCode);
        if (StrUtil.isNotBlank(orgId)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getOrgId, orgId);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getOrgId);
        }
        if (StrUtil.isNotBlank(clientCode)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getClientCode, clientCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(contractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getContractCode, contractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getContractCode);
        }
        if (StrUtil.isNotBlank(billContractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getBillContractCode, billContractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getBillContractCode);
        }
        lambdaQueryWrapper.orderByDesc(ContractBalanceEntity::getId);
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
    public void insertContractBalance(String sceneCode, String systemCode, Long voucherId, String businessCode, List<AccountEntity> accountEntityList, Map<String, Object> fundTypeMap, Map<String, Object> lastBalanceMap, String orgId, String contractCode, String clientCode, String voucherDate, String easVoucherId, Integer periodCode, String billContractCode) {
        if (lastBalanceMap.get("id") == null) {
            //如果没有以前的合同余额数据，则直接insert新的
            Map<String, Object> rowMap = this.buildNewContractBalanceMap(sceneCode, systemCode, voucherId, businessCode, fundTypeMap, lastBalanceMap, orgId, contractCode, clientCode, voucherDate, easVoucherId, periodCode, billContractCode);
            //插入合同余额表
            this.insertMap(rowMap);
            //插入最新的合同余额表
            contractBalanceLatestService.insertMap(rowMap);
            return;
        }
//        Map<String, String> fundTypeAccountMap = accountEntityList.stream().collect(Collectors.toMap(e->e.getFundType(), e -> e.getDebitCreditType()));
        Map<String, List<AccountEntity>>  fundTypeAccountListMap = accountEntityList.stream().collect(Collectors.groupingBy(AccountEntity::getFundType));
        Map<String, Object> newRowMap = new HashMap<>();
        newRowMap.put("business_code", businessCode);
        newRowMap.put("system_code", systemCode);
        newRowMap.put("voucher_id", voucherId);
        newRowMap.put("period_code", periodCode);
        if (voucherDate != null){
            newRowMap.put("business_date", LocalDateTimeUtil.parse(voucherDate,"yyyy-MM-dd"));
            newRowMap.put("voucher_date", LocalDateTimeUtil.parse(voucherDate,"yyyy-MM-dd"));
        }
        newRowMap.put("scene_code", sceneCode);
        newRowMap.put("org_id", orgId);
        newRowMap.put("contract_code", contractCode);
        newRowMap.put("client_code", clientCode);
        newRowMap.put("eas_voucher_id", easVoucherId);
        newRowMap.put("bill_contract_code", billContractCode);

        for (String key : lastBalanceMap.keySet()) {
            newRowMap.put(key, lastBalanceMap.get(key));
        }
        for (String fundType : fundTypeMap.keySet()) {
            String balanceKey = fundType + "_balance";
            String amountKey = fundType + "_amount";
            newRowMap.put(amountKey, fundTypeMap.get(fundType));
            if (lastBalanceMap.containsKey(balanceKey)) {
                BigDecimal balance = (BigDecimal) lastBalanceMap.get(balanceKey);
                BigDecimal amount = (BigDecimal) fundTypeMap.get(fundType);
//                String debitCreditType = fundTypeAccountListMap.get(fundType).get(0).getDebitCreditType();
//                int directe = 1;
//                if ("CR".equals(debitCreditType)){
//                    directe = -1;
//                }
                //计算新的余额，然后覆盖
                BigDecimal newBalance = NumberUtil.add(balance,amount);
                newRowMap.put(balanceKey, newBalance);
            }
        }
        //更新最新的合同余额表
        contractBalanceLatestService.updateLastBalanceMap(newRowMap);
        //插入合同余额表
        this.insertMap(newRowMap);
    }

    /**
     * 构建新的记录map
     *
     * @param fundTypeMap
     * @return
     */
    private Map<String, Object> buildNewContractBalanceMap(String sceneCode, String systemCode, Long voucherId, String businessCode, Map<String, Object> fundTypeMap, Map<String, Object> lastBalanceMap, String orgId, String contractCode, String clientCode, String voucherDate, String easVoucherId, Integer periodCode, String billContractCode) {
        Map<String, Object> rowMap = new HashMap<>();
        for (String key : fundTypeMap.keySet()) {
            //发生额字段
            rowMap.put(key + "_amount", fundTypeMap.get(key));
            //余额字段
            rowMap.put(key + "_balance", fundTypeMap.get(key));
        }
        for (String key : lastBalanceMap.keySet()) {
            rowMap.put(key, lastBalanceMap.get(key));
        }
        rowMap.put("business_code", businessCode);
        rowMap.put("system_code", systemCode);
        rowMap.put("voucher_id", voucherId);
        rowMap.put("eas_voucher_id", easVoucherId);
        rowMap.put("period_code", periodCode);
        rowMap.put("bill_contract_code", billContractCode);

        if (voucherDate != null){
            rowMap.put("business_date", LocalDateTimeUtil.parse(voucherDate,"yyyy-MM-dd"));
            rowMap.put("voucher_date", LocalDateTimeUtil.parse(voucherDate,"yyyy-MM-dd"));
        }
        rowMap.put("scene_code", sceneCode);
        rowMap.put("org_id", orgId);
        rowMap.put("contract_code", contractCode);
        rowMap.put("client_code", clientCode);
        return rowMap;
    }

}

