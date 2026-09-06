package com.utfinancing.financehub.engine.finance.service.impl;

import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordTempEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractStatusRecordTempMapper;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordTempService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-01-17
 * @Description :  ContractStatusRecordTemp服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractStatusRecordTempServiceImpl extends ServiceImpl<ContractStatusRecordTempMapper,
        ContractStatusRecordTempEntity> implements IContractStatusRecordTempService {

    private final ContractStatusRecordTempMapper contractStatusRecordTempMapper;


}

