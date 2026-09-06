package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanTempEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanTempMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanTempService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : hzhao
 * @Date : Create in 2023-12-20
 * @Description :  RepaymentPlanTemp服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RepaymentPlanTempServiceImpl extends ServiceImpl<RepaymentPlanTempMapper, RepaymentPlanTempEntity> implements IRepaymentPlanTempService {

    private final RepaymentPlanTempMapper repaymentPlanTempMapper;


    @Override
    public void deleteAllCompleteData() {
        repaymentPlanTempMapper.deleteAllCompleteData();
//        repaymentPlanTempMapper.truncateTable();
    }

    @Override
    public void copyDataFromRepaymentPlan(LeaseIncomeQueryDTO queryDTO) {
        repaymentPlanTempMapper.copyDataFromRepaymentPlan(queryDTO);
    }
}

