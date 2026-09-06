package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.AccountBalanceSheetExcelExportDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description : ServiceFeePlan服务类接口
 * @Modified :
 */
public interface IServiceFeePlanService extends IService<ServiceFeePlanEntity> {

    Long saveServiceFeePlan(ServiceFeePlanDTO dto);

    Long updateServiceFeePlan(Long id, ServiceFeePlanDTO dto);

    ServiceFeePlanDTO getServiceFeePlanDTOById(Long id);

    IPage<ServiceFeePlanVO> selectPage(ServiceFeePlanQueryDTO queryDTO);

    List<ServiceFeePlanEntity> selectByContractCode(String contractCode);

    /**
     * 物理删除服务费计划
     * @param contractCode
     */
    void physicalDeleteByContractCode(String contractCode);

    List<ServiceFeePlanVO> export(ServiceFeePlanQueryDTO queryDTO);

    List<ServiceFeeAllocationExcelVo> getAllAllocationsPlanList(ServiceFeeQueryDTO queryDTO);
}
