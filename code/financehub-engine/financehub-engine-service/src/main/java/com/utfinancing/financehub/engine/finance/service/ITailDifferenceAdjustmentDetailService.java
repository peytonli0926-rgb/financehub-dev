package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description : TailDifferenceAdjustmentDetail服务类接口
 * @Modified :
 */
public interface ITailDifferenceAdjustmentDetailService extends IService<TailDifferenceAdjustmentDetailEntity> {

    Long saveTailDifferenceAdjustmentDetail(TailDifferenceAdjustmentDetailDTO dto);

    Long updateTailDifferenceAdjustmentDetail(Long id, TailDifferenceAdjustmentDetailDTO dto);

    TailDifferenceAdjustmentDetailDTO getTailDifferenceAdjustmentDetailDTOById(Long id);

    IPage<TailDifferenceAdjustmentDetailVO> selectPage(TailDifferenceAdjustmentDetailQueryDTO queryDTO);

    Boolean removeBatcheByDetailId(List<Long> idList);

    List<TailDifferenceAdjustmentDetailVO> selectByCondition(TailDifferenceAdjustmentDetailQueryDTO queryDTO);

    boolean initInsertData(TailDifferenceAdjustmentQueryDTO queryDTO);

    List<TailDifferenceAdjustmentEntity> selectAllOrgIdAccountCode();

    List<TailDifferenceAdjustmentDetailVO> selectByParams(TailDifferenceAdjustmentDetailQueryDTO queryDTO);

    void generateContractBalanceTempData(TailDifferenceAdjustmentQueryDTO queryDTO);

    void truncateContractBalanceTempData();

}
