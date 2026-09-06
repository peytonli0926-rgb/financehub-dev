package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHisEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;

import java.util.List;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description : RepaymentPlanHis服务类接口
 * @Modified :
 */
public interface IRepaymentPlanHisService extends IService<RepaymentPlanHisEntity> {

    /**
     * 偿还计划历史版本存储
     */
    public void saveRepaymentPlanHisBatch(List<RepaymentPlanSaveDTO> dtos);
}
