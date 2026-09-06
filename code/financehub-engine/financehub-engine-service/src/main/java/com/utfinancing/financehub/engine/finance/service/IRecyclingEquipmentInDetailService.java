package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInDetailExportExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RecyclingEquipmentInQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInDetailVO;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : RecyclingEquipmentInDetail服务类接口
 * @Modified :
 */
public interface IRecyclingEquipmentInDetailService extends IService<RecyclingEquipmentInDetailEntity> {

    Long saveRecyclingEquipmentInDetail(RecyclingEquipmentInDetailDTO dto);

    Long updateRecyclingEquipmentInDetail(Long id, RecyclingEquipmentInDetailDTO dto);

    RecyclingEquipmentInDetailDTO getRecyclingEquipmentInDetailDTOById(Long id);

    IPage<RecyclingEquipmentInDetailVO> selectPage(RecyclingEquipmentInDetailQueryDTO queryDTO);

    List<RecyclingEquipmentInCheckVO> getCheckData(String inboundDate, String orgId,Long id);

    List<RecyclingEquipmentInDetailExportExcelDTO> listByCondition(RecyclingEquipmentInDetailQueryDTO queryDTO);

    RecyclingEquipmentInDetailVO getProvision(String contractCode, String orgId);
}
