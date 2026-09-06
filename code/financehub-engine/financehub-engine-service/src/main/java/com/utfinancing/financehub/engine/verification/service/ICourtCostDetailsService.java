package com.utfinancing.financehub.engine.verification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsDTO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import com.utfinancing.financehub.engine.verification.entity.CourtCostDetailsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description : CourtCostDetails服务类接口
 * @Modified :
 */
public interface ICourtCostDetailsService extends IService<CourtCostDetailsEntity> {

    Long saveCourtCostDetails(CourtCostDetailsDTO dto);

    Long updateCourtCostDetails(Long id, CourtCostDetailsDTO dto);

    CourtCostDetailsDTO getCourtCostDetailsDTOById(Long id);

    IPage<CourtCostDetailsVO> selectPage(CourtCostDetailsQueryDTO queryDTO);

    List<CourtCostDetailsVO> listByCondition(CourtCostDetailsQueryDTO queryDTO);

    Boolean removeBatchByCourtCostIdList(List<Long> courtCostIdList);

    IPage<CourtCostReportFormVO> selectReportFormPage(CourtCostDetailsQueryDTO queryDTO);

    List<CourtCostReportFormVO> selectReportFormDetails(CourtCostDetailsQueryDTO queryDTO);

}
