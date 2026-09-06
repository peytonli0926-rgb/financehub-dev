package com.utfinancing.financehub.engine.finance.service.impl;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanChangeRecordsEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanChangeRecordsMapper;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanChangeRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author : robjiang
 * @Date : Create in 2024-05-23
 * @Description :  RepaymentPlanChangeRecords服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RepaymentPlanChangeRecordsServiceImpl extends ServiceImpl<RepaymentPlanChangeRecordsMapper,
        RepaymentPlanChangeRecordsEntity> implements IRepaymentPlanChangeRecordsService {

    private final RepaymentPlanChangeRecordsMapper repaymentPlanChangeRecordsMapper;


}

