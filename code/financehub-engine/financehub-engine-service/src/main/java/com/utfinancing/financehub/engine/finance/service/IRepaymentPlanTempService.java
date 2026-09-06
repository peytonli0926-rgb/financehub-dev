package com.utfinancing.financehub.engine.finance.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanTempEntity;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeQueryDTO;

/**
 * @Author : hzhao
 * @Date : Create in 2023-12-20
 * @Description : RepaymentPlanTemp服务类接口
 * @Modified :
 */
public interface IRepaymentPlanTempService extends IService<RepaymentPlanTempEntity> {


    void deleteAllCompleteData();

    void copyDataFromRepaymentPlan(LeaseIncomeQueryDTO queryDTO);
}
