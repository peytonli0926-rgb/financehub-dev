package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.ContractBalanceTempEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2024-02-05
 * @Description : ContractBalanceTemp服务类接口
 * @Modified :
 */
public interface IContractBalanceTempService extends IService<ContractBalanceTempEntity> {

    public void insertMap(Map<String, Object> rowMap);

    public int delBalanceTempByVoucherId(List<Long> idList);

    public void delBalanceTeamData(Map<String, Object> rowMap);
}
