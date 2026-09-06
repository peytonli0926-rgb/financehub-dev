package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.FundSystemBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.FundSystemBalanceEntity;
import com.utfinancing.financehub.engine.finance.mapper.FundSystemBalanceMapper;
import com.utfinancing.financehub.engine.finance.service.IFundSystemBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-05
 * @Description :  FundSystemBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class FundSystemBalanceServiceImpl extends ServiceImpl<FundSystemBalanceMapper, FundSystemBalanceEntity> implements IFundSystemBalanceService {

    private final FundSystemBalanceMapper fundSystemBalanceMapper;

    @Resource
    private IAccountService accountService;

    @Override
    public Long saveFundSystemBalance(FundSystemBalanceDTO dto) {
        FundSystemBalanceEntity entity = BeanUtil.copyProperties(dto, FundSystemBalanceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateFundSystemBalance(Long id, FundSystemBalanceDTO dto) {
        FundSystemBalanceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundSystemBalanceDTO getFundSystemBalanceDTOById(Long id) {
        FundSystemBalanceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundSystemBalanceDTO.class);
    }

    @Override
    public IPage<FundSystemBalanceVO> selectPage(FundSystemBalanceQueryDTO queryDTO) {
        LambdaQueryWrapper<FundSystemBalanceEntity> queryWrapper = Wrappers.<FundSystemBalanceEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundSystemBalanceEntity> entityIPage = fundSystemBalanceMapper.selectPage(new Page<FundSystemBalanceEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundSystemBalanceVO.class);
    }

    @Override
    public Boolean saveFundSystemBalanceFromVoucher(VoucherDTO voucherDTO) {
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        List<VoucherEntryDTO> voucherEntryList = voucherDTO.getEntryList();
        String bankNo = voucherDTO.getBankNo();
        Map<String, BigDecimal> fundTypeMap = new HashMap<>();
        for (VoucherEntryDTO entryDTO : voucherEntryList) {
            BigDecimal currentAmount;
            if (DRCREnum.DR.getCode().equals(entryDTO.getDebitCreditType())){
                //余额方向为借方 发生额 = 借方金额 - 贷方金额
                currentAmount = NumberUtil.sub(entryDTO.getDebitAmount(), entryDTO.getCreditAmount());
            } else{
                currentAmount = NumberUtil.sub(entryDTO.getCreditAmount(), entryDTO.getDebitAmount());
            }
            fundTypeMap.put(entryDTO.getFundType(), NumberUtil.add(fundTypeMap.get(entryDTO.getFundType()), currentAmount));
        }
        insertFundSystemBalance(fundTypeMap, getLastBalanceMap(bankNo), voucherDTO,accountEntityList);
        return Boolean.TRUE;
    }

    public void insertFundSystemBalance(Map<String, BigDecimal> fundTypeMap, Map<String, Object> lastBalanceMap, VoucherDTO voucherDTO,List<AccountEntity> accountEntityList){
        Map<String, List<AccountEntity>>  fundTypeAccountListMap = accountEntityList.stream().collect(Collectors.groupingBy(AccountEntity::getFundType));
        if (lastBalanceMap.get("id") == null) {
            //直接新增
            Map<String, Object> fundMap = buildNewContractBalanceMap(fundTypeMap,lastBalanceMap,voucherDTO);
            insertMap(fundMap);
            return;
        }
        Map<String, Object> newRowMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = lastBalanceMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newRowMap.put(entry.getKey(), entry.getValue());
        }
        newRowMap.put(FundBalanceColumnsEnum.VOUCHER_ID.getCode(), voucherDTO.getId());
        newRowMap.put(FundBalanceColumnsEnum.ORDER_ID.getCode(), voucherDTO.getOrderId());
        newRowMap.put(FundBalanceColumnsEnum.BANK_NO.getCode(), voucherDTO.getBankNo());
        newRowMap.put(FundBalanceColumnsEnum.VOUCHER_DATE.getCode(), voucherDTO.getVoucherDate());
        newRowMap.put(FundBalanceColumnsEnum.TRANSACTION_TYPE.getCode(), voucherDTO.getTransactionType());
        for (String fundType : fundTypeMap.keySet()) {
            String balanceKey = fundType + "_balance";
            String amountKey = fundType + "_amount";
            newRowMap.put(amountKey, fundTypeMap.get(fundType));
            if (lastBalanceMap.containsKey(balanceKey)) {
                BigDecimal balance = (BigDecimal) lastBalanceMap.get(balanceKey);
                BigDecimal amount = fundTypeMap.get(fundType);
                //余额 = 余额 + 发生额
//                String debitCreditType = fundTypeAccountListMap.get(fundType).get(0).getDebitCreditType();
//                int directe = 1;
//                if ("CR".equals(debitCreditType)){
//                    directe = -1;
//                }
                //计算新的余额，然后覆盖
                BigDecimal newBalance = NumberUtil.add(balance, amount);
                newRowMap.put(balanceKey, newBalance);
            }
        }
        this.insertMap(newRowMap);
    }

    private void insertMap(Map<String, Object> rowMap) {
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
        fundSystemBalanceMapper.insertFundSystemBalance(columns, values);
    }

    private Map<String, Object> buildNewContractBalanceMap(Map<String, BigDecimal> fundTypeMap, Map<String, Object> lastBalanceMap, VoucherDTO voucherDTO) {
        Map<String, Object> rowMap = new HashMap<>();
        Iterator<Map.Entry<String, BigDecimal>> it = fundTypeMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, BigDecimal> entry = it.next();
            //发生额字段
            rowMap.put(entry.getKey() + "_amount", entry.getValue());
            //余额字段
            rowMap.put(entry.getKey() + "_balance", entry.getValue());
        }
        rowMap.put(FundBalanceColumnsEnum.VOUCHER_ID.getCode(), voucherDTO.getId());
        rowMap.put(FundBalanceColumnsEnum.VOUCHER_DATE.getCode(), voucherDTO.getVoucherDate());
        rowMap.put(FundBalanceColumnsEnum.BANK_NO.getCode(), voucherDTO.getBankNo());
        rowMap.put(FundBalanceColumnsEnum.ORDER_ID.getCode(), voucherDTO.getOrderId());
        rowMap.put(FundBalanceColumnsEnum.TRANSACTION_TYPE.getCode(), voucherDTO.getTransactionType());
        return rowMap;
    }

    public Map<String,Object> getLastBalanceMap(String bankNo){
        //根据银行账号来获取最新的余额
        QueryWrapper<FundSystemBalanceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FundSystemBalanceEntity::getBankNo, bankNo);
        queryWrapper.lambda().orderByDesc(FundSystemBalanceEntity::getId);
        queryWrapper.lambda().last("limit 1");
        Map<String,Object> resultMap = this.getMap(queryWrapper);
        if (MapUtil.isEmpty(resultMap)) {
            Map<String,Object> emptyMap = Maps.newHashMap();
            emptyMap.put(FundBalanceColumnsEnum.BANK_NO.getCode(),bankNo);
            return emptyMap;
        }
        return resultMap;
    }

}

