package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.ContractTaAmountEntity;
import com.utfinancing.financehub.etl.financial.mapper.ContractTaAmountMapper;
import com.utfinancing.financehub.etl.financial.service.IContractTaAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}

