package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailContractBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-05-08
 * @Description : CheckAccountDetailContractBalance服务类接口
 * @Modified :
 */
public interface ICheckAccountDetailContractBalanceService extends IService<CheckAccountDetailContractBalanceEntity> {

    List<CheckAccountDetailContractBalanceEntity> selectContractTmpTableData(Integer periodCode);

    void saveTmpBatch(List<CheckAccountDetailContractBalanceEntity> list, Integer periodCode);
}
