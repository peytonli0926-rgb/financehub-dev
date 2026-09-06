package com.utfinancing.financehub.engine.claim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderSpecialEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description : ClaimOrderSpecial服务类接口
 * @Modified :
 */
public interface IClaimOrderSpecialService extends IService<ClaimOrderSpecialEntity> {

    Long saveClaimOrderSpecial(ClaimOrderSpecialDTO dto);

    Long updateClaimOrderSpecial(Long id, ClaimOrderSpecialDTO dto);

    ClaimOrderSpecialDTO getClaimOrderSpecialDTOById(Long id);

    IPage<ClaimOrderSpecialVO> selectPage(ClaimOrderSpecialQueryDTO queryDTO);

    List<ClaimOrderSpecialVO> selectByCondition(ClaimOrderSpecialQueryDTO queryDTO);

    List<ClaimOrderCostVo> statisticalCost(ClaimOrderSpecialQueryDTO queryDTO);

    List<ClaimOrderSpecialVO> getOrderByContractCodeList(List<String> contractList);

}
