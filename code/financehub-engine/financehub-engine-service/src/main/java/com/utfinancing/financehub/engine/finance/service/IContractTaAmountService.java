package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractTaAmountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanExceldataEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-26
 * @Description : ContractTaAmount服务类接口
 * @Modified :
 */
public interface IContractTaAmountService extends IService<ContractTaAmountEntity> {
    /**
     * 期初数据ta金额保存
     */
    public void saveTaAmountByExcelData(List<RepaymentPlanExceldataEntity> result);

    /**
     * 取得合同的ta金额
     */
    public Map<String, BigDecimal> getTaAmountMap(Map<String, ContractEntity> contractEntityMap);
}
