package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanEntity;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description : RepaymentPlan服务类接口
 * @Modified :
 */
public interface IRepaymentPlanService extends IService<RepaymentPlanEntity> {
    /**
     * 根据合同取得偿还计划
     */
    public List<RepaymentPlanEntity> selectByContractCode(String contractCode);


    /**
     * 根据合同编码删除偿还计划
     */
    public void delRepaymentPlanByContractCode(List<String> contractCodeList);
}
