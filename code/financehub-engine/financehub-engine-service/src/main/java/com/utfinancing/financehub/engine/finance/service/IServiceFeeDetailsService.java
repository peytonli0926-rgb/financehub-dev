package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description : ServiceFeeDetails服务类接口
 * @Modified :
 */
public interface IServiceFeeDetailsService extends IService<ServiceFeeDetailsEntity> {

    Long saveServiceFeeDetails(ServiceFeeDetailsDTO dto);

    Long updateServiceFeeDetails(Long id, ServiceFeeDetailsDTO dto);

    ServiceFeeDetailsDTO getServiceFeeDetailsDTOById(Long id);

    IPage<ServiceFeeDetailsVO> selectPage(ServiceFeeDetailsQueryDTO queryDTO);

    List<ServiceFeeDetailsEntity> selectLastPeriodData(ServiceFeeDetailsVO serviceFeeDetailsVO);

    void updateVoucherStatus(List<Long> ids, String code, Integer periodCode);
}
