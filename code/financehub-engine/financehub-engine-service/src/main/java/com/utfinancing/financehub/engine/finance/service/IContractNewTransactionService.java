package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.ContractEntity;

import java.util.List;
import java.util.Map;

public interface IContractNewTransactionService {

    /**
     * 批量更新合同信息-根据ID
     */
    public void batchUpdateById(List<ContractEntity> contractEntityList);

    /**
     * 业务数据生成凭证时更新合同信息
     */
    public String saveOrUpdateContract(Map<String, Object> interfaceDataMap);
}
