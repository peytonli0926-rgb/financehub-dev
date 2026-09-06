package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanCostQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanCostDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanCostVO;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanCostEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-12-28
 * @Description : RepaymentPlanCost服务类接口
 * @Modified :
 */
public interface IRepaymentPlanCostService extends IService<RepaymentPlanCostEntity> {

    Long saveRepaymentPlanCost(RepaymentPlanCostDTO dto);

    Long updateRepaymentPlanCost(Long id, RepaymentPlanCostDTO dto);

    RepaymentPlanCostDTO getRepaymentPlanCostDTOById(Long id);

    IPage<RepaymentPlanCostVO> selectPage(RepaymentPlanCostQueryDTO queryDTO);

    List<RepaymentPlanCostEntity> selectList(RepaymentPlanCostQueryDTO queryDTO);
}
