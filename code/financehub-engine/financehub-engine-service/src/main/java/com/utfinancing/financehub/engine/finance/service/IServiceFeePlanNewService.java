package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanNewVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanNewEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;

import java.util.List;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description : ServiceFeePlanNew服务类接口
 * @Modified :
 */
public interface IServiceFeePlanNewService extends IService<ServiceFeePlanNewEntity> {

    Long saveServiceFeePlanNew(ServiceFeePlanNewDTO dto);

    Long updateServiceFeePlanNew(Long id, ServiceFeePlanNewDTO dto);

    ServiceFeePlanNewDTO getServiceFeePlanNewDTOById(Long id);

    IPage<ServiceFeePlanNewVO> selectPage(ServiceFeePlanNewQueryDTO queryDTO);

    void calculationServiceFeePlanNew(ServiceFeeQueryDTO queryDTO);

    List<ServiceFeePlanNewVO> selectDetailPlanList(ServiceFeeDetailsQueryDTO queryDTO);

    List<ServiceFeeAllocationExcelVo> getAllAllocationsPlanList(ServiceFeeQueryDTO queryDTO);
}
