package com.utfinancing.financehub.engine.claim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderPaymentQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderPaymentDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderPaymentVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderPaymentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description : ClaimOrderPayment服务类接口
 * @Modified :
 */
public interface IClaimOrderPaymentService extends IService<ClaimOrderPaymentEntity> {

    Long saveClaimOrderPayment(ClaimOrderPaymentDTO dto);

    Long updateClaimOrderPayment(Long id, ClaimOrderPaymentDTO dto);

    ClaimOrderPaymentDTO getClaimOrderPaymentDTOById(Long id);

    IPage<ClaimOrderPaymentVO> selectPage(ClaimOrderPaymentQueryDTO queryDTO);

}
