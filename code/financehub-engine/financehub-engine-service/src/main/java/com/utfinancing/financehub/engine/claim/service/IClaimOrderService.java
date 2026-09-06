package com.utfinancing.financehub.engine.claim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailSaveDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSaveDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description : ClaimOrder服务类接口
 * @Modified :
 */
public interface IClaimOrderService extends IService<ClaimOrderEntity> {

    Long saveClaimOrder(ClaimOrderDTO dto);

    Long updateClaimOrder(Long id, ClaimOrderDTO dto);

    ClaimOrderDTO getClaimOrderDTOById(Long id);

    IPage<ClaimOrderVO> selectPage(ClaimOrderQueryDTO queryDTO);

    Long saveOrderTransactionData(ClaimOrderSaveDTO saveDTO);

    List<ClaimOrderVO> selectByCondition(ClaimOrderQueryDTO queryDTO);

}
