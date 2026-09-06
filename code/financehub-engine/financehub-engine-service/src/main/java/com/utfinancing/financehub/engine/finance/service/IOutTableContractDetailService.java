package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutTableContractDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description : OutTableContractDetail服务类接口
 * @Modified :
 */
public interface IOutTableContractDetailService extends IService<OutTableContractDetailEntity> {

    Long saveOutTableContractDetail(OutTableContractDetailDTO dto);

    Long updateOutTableContractDetail(Long id, OutTableContractDetailDTO dto);

    OutTableContractDetailDTO getOutTableContractDetailDTOById(Long id);

    IPage<OutTableContractDetailVO> selectPage(OutTableContractDetailQueryDTO queryDTO);

    Boolean removeBatcheByDetailId(List<Long> idList);

    List<OutTableContractDetailEntity> getOutTableContractDetailInfoByTaId(Long id);

    List<OutTableContractDetailVO> selectDetailsByParams(OutTableContractDetailQueryDTO queryDTO);
}
