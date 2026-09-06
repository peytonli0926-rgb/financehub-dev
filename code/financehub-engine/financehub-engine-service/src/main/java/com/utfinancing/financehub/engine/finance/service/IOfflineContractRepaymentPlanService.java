package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.entity.OfflineContractRepaymentPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : OfflineContractRepaymentPlan服务类接口
 * @Modified :
 */
public interface IOfflineContractRepaymentPlanService extends IService<OfflineContractRepaymentPlanEntity> {

    Long saveOfflineContractRepaymentPlan(OfflineContractRepaymentPlanDTO dto);

    Long updateOfflineContractRepaymentPlan(Long id, OfflineContractRepaymentPlanDTO dto);

    OfflineContractRepaymentPlanDTO getOfflineContractRepaymentPlanDTOById(Long id);

    void deleteByContractCodeList(List<String> ids);

    IPage<OfflineContractRepaymentPlanVO> selectPage(OfflineContractRepaymentPlanQueryDTO queryDTO);

    List<OfflineContractRepaymentPlanVO> selectList(OfflineContractRepaymentPlanQueryDTO queryDTO);
}
