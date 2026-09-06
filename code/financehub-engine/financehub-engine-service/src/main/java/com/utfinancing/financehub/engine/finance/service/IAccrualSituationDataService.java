package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.AccrualSituationDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-06-30
 * @Description : AccrualSituationData服务类接口
 * @Modified :
 */
public interface IAccrualSituationDataService extends IService<AccrualSituationDataEntity> {


    AccrualSituationDataEntity getDataByVoucherId(Long voucherId, String fillType);
}
