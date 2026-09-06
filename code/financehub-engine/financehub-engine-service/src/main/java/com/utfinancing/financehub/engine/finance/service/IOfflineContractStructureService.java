package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractStructureVO;
import com.utfinancing.financehub.engine.finance.entity.OfflineContractStructureEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : OfflineContractStructure服务类接口
 * @Modified :
 */
public interface IOfflineContractStructureService extends IService<OfflineContractStructureEntity> {

    Long saveOfflineContractStructure(OfflineContractStructureDTO dto);

    Long updateOfflineContractStructure(Long id, OfflineContractStructureDTO dto);

    OfflineContractStructureDTO getOfflineContractStructureDTOById(Long id);

    OfflineContractStructureDTO getOfflineContractStructureDTOByContractCode(String contractCode, String contractCodeM);

    IPage<OfflineContractStructureVO> selectPage(OfflineContractStructureQueryDTO queryDTO);

    List<OfflineContractStructureVO> selectList(OfflineContractStructureQueryDTO queryDTO);
}
