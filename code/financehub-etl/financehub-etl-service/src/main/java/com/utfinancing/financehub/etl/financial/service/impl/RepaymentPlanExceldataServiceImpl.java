package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanExceldataEntity;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanExceldataMapper;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanExceldataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-23
 * @Description :  RepaymentPlanExceldata服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RepaymentPlanExceldataServiceImpl extends ServiceImpl<RepaymentPlanExceldataMapper, RepaymentPlanExceldataEntity>
        implements IRepaymentPlanExceldataService {

    private final RepaymentPlanExceldataMapper repaymentPlanExceldataMapper;


}

