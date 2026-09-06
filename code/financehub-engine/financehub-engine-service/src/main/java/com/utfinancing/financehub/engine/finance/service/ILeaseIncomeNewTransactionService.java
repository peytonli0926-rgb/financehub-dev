package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description : LeaseIncome服务类接口
 * @Modified :
 */
public interface ILeaseIncomeNewTransactionService {


    /**
     * 收益计提生成凭证
     */
    public boolean genVoucher(String isSubmit, List<LeaseIncomeDetailsEntity> detailsEntities,
                           Map<String, ContractDTO> contractMap);



    /**
     * 收益计提生成凭证
     */
    public boolean genVoucher1(String isSubmit, List<LeaseIncomeDetailsEntity> detailsEntities,
                                Map<String, Object> params);
}
