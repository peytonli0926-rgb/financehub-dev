package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.ServiceNoAmortizationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author : robjiang
 * @Date : Create in 2024-06-05
 * @Description : ServiceNoAmortization服务类接口
 * @Modified :
 */
public interface IServiceNoAmortizationService extends IService<ServiceNoAmortizationEntity> {

    /**
     * 取得合同的期初服务费
     */
    public Map<String, BigDecimal> getServiceNoAmortizationAmount(Set<String> contractCodeList);

    /**
     * 取得待摊销金额
     */
    public BigDecimal getToBeAssessedAmount(String contractCode);
}
