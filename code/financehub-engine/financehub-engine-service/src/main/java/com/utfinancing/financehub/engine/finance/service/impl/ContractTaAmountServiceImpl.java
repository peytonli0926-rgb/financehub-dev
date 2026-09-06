package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.LeaseTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractTaAmountEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractTaAmountMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectTaAmountInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectTaAmountOutputDTO;
import com.utfinancing.financehub.engine.finance.service.IContractTaAmountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-26
 * @Description :  ContractTaAmount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractTaAmountServiceImpl extends ServiceImpl<ContractTaAmountMapper, ContractTaAmountEntity>
        implements IContractTaAmountService {

    private final ContractTaAmountMapper contractTaAmountMapper;

    /**
     * 创建ta金额对象
     */
    public ContractTaAmountEntity setContractTaAmountEntity(RepaymentPlanExceldataEntity exceldataEntity) {
        ContractTaAmountEntity entity = new ContractTaAmountEntity();
        entity.setId(IdWorker.getId());
        entity.setContractCode(exceldataEntity.getContractCode());
        entity.setTaAmount(exceldataEntity.getTa());
        entity.setTransactionDate(new Date());
        return entity;
    }

    /**
     * 期初数据ta金额保存
     */
    public void saveTaAmountByExcelData(List<RepaymentPlanExceldataEntity> result) {
        if (result == null || result.isEmpty()) {
            return ;
        }

        List<String> contractCodeList = result.stream().map(RepaymentPlanExceldataEntity::getContractCode).distinct().
                collect(Collectors.toList());
        LambdaQueryWrapper<ContractTaAmountEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(ContractTaAmountEntity::getContractCode, contractCodeList);
        contractTaAmountMapper.delete(wrapper);

        List<ContractTaAmountEntity> contractTaAmountEntityList = new ArrayList<>();
        for (RepaymentPlanExceldataEntity entity : result) {
            if (entity.getTa() != null &&  entity.getTa().compareTo(new BigDecimal(0)) != 0) {
                contractTaAmountEntityList.add(this.setContractTaAmountEntity(entity));
            }
        }
        if (contractTaAmountEntityList != null && !contractTaAmountEntityList.isEmpty()) {
            saveBatch(contractTaAmountEntityList);
        }
    }

    /**
     * 取得合同的ta金额
     */
    public Map<String, BigDecimal> getTaAmountMap(Map<String, ContractEntity> contractEntityMap) {
        SelectTaAmountInputDTO params = new SelectTaAmountInputDTO();
        params.setContractCodeList(contractEntityMap.keySet().stream().collect(Collectors.toList()));
        List<SelectTaAmountOutputDTO> selectTaAmountList = contractTaAmountMapper.selectTaAmount(params);
        if (selectTaAmountList == null || selectTaAmountList.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, BigDecimal> taAmountMap = new HashMap<>();
        for (SelectTaAmountOutputDTO dto : selectTaAmountList) {
            ContractEntity entity = contractEntityMap.get(dto.getBusKey());
            if (entity == null) {
                taAmountMap.put(dto.getContractCode(), BigDecimal.ZERO);
            } else {
                if (YesOrNoEnum.NO.getCode().equals(entity.getIsChangeRepayment())) {
                    // TA的金额除去税收
                    if (LeaseTypeEnum.DIRECT.getCode().equals(entity.getLeaseType())) {
                        BigDecimal taAmount = dto.getSumTaAmount().divide(Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
                        taAmountMap.put(dto.getContractCode(), taAmount);
                    } else {
                        taAmountMap.put(dto.getContractCode(), dto.getSumTaAmount());
                    }
                } else {
                    taAmountMap.put(dto.getContractCode(), dto.getSumTaAmount());
                }
            }
        }
        return taAmountMap;
    }
}

