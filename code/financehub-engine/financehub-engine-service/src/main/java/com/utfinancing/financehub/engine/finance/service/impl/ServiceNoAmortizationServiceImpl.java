package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.utfinancing.financehub.engine.finance.entity.ServiceNoAmortizationEntity;
import com.utfinancing.financehub.engine.finance.mapper.ServiceNoAmortizationMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectAssessedAmountInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectAssessedAmountOutputDTO;
import com.utfinancing.financehub.engine.finance.service.IServiceNoAmortizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-06-05
 * @Description :  ServiceNoAmortization服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceNoAmortizationServiceImpl extends ServiceImpl<ServiceNoAmortizationMapper,
        ServiceNoAmortizationEntity> implements IServiceNoAmortizationService {

    private final ServiceNoAmortizationMapper serviceNoAmortizationMapper;

    /**
     * 取得合同的待摊销金额(不含咨询服务费)
     */
    public Map<String, BigDecimal> getServiceNoAmortizationAmount(Set<String> contractCodeList) {
        if (contractCodeList == null || contractCodeList.isEmpty()) {
            return new HashMap<>();
        }
        SelectAssessedAmountInputDTO inputDTO = new SelectAssessedAmountInputDTO();
        inputDTO.setContractCodeList(contractCodeList.stream().collect(Collectors.toList()));
        List<SelectAssessedAmountOutputDTO> dtoList = serviceNoAmortizationMapper.selectAssessedAmount(inputDTO);
        if (dtoList == null || dtoList.isEmpty()) {
            return new HashMap<>();
        }
        return dtoList.stream().filter(e->e.getAssessedAmount() != null).
                collect(Collectors.toMap(e->e.getContractCode(), e->e.getAssessedAmount(), (a,b) -> a.add(b)));
    }

    /**
     * 取得待摊销金额
     */
    public BigDecimal getToBeAssessedAmount(String contractCode) {
        Set<String> contractCodeSets = new HashSet<>();
        contractCodeSets.add(contractCode);
        Map<String, BigDecimal> serviceNoAmortizationMap = this.getServiceNoAmortizationAmount(contractCodeSets);
        BigDecimal toBeAssessedAmount = serviceNoAmortizationMap.get(contractCode);
        if (toBeAssessedAmount == null) {
            toBeAssessedAmount = BigDecimal.ZERO;
        }
        return toBeAssessedAmount;
    }
}

