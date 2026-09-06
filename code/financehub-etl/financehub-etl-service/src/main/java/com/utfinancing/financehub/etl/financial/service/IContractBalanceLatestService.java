package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.entity.ContractBalanceLatestEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-02
 * @Description : ContractBalanceLatest服务类接口
 * @Modified :
 */
public interface IContractBalanceLatestService extends IService<ContractBalanceLatestEntity> {


    Map<String, Object> getLastBalanceMap(String businessCode, String orgId, String clientCode, String contractCode, String billContractCode);

    void insertMap(Map<String, Object> rowMap);

    void updateLastBalanceMap(Map<String, Object> rowMap);

}
