package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferPlanVO;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : ConvertTransferPlan服务类接口
 * @Modified :
 */
public interface IConvertTransferPlanService extends IService<ConvertTransferPlanEntity> {

    Long saveConvertTransferPlan(ConvertTransferPlanDTO dto);

    Long updateConvertTransferPlan(Long id, ConvertTransferPlanDTO dto);

    ConvertTransferPlanDTO getConvertTransferPlanDTOById(Long id);

    IPage<ConvertTransferPlanVO> selectPage(ConvertTransferPlanQueryDTO queryDTO);

    void deleteByConvertTransferId(List<Long> ids);
}
