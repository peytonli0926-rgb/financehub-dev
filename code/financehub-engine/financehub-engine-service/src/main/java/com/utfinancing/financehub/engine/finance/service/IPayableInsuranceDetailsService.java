package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description : PayableInsuranceDetails服务类接口
 * @Modified :
 */
public interface IPayableInsuranceDetailsService extends IService<PayableInsuranceDetailsEntity> {

    Long savePayableInsuranceDetails(PayableInsuranceDetailsDTO dto);

    Long updatePayableInsuranceDetails(Long id, PayableInsuranceDetailsDTO dto);

    PayableInsuranceDetailsDTO getPayableInsuranceDetailsDTOById(Long id);

    IPage<PayableInsuranceDetailsVO> selectPage(PayableInsuranceDetailsQueryDTO queryDTO);

}
