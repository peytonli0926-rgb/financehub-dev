package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentVO;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description : TailDifferenceAdjustment服务类接口
 * @Modified :
 */
public interface ITailDifferenceAdjustmentService extends IService<TailDifferenceAdjustmentEntity> {

    Long saveTailDifferenceAdjustment(TailDifferenceAdjustmentDTO dto);

    Long updateTailDifferenceAdjustment(Long id, TailDifferenceAdjustmentDTO dto);

    TailDifferenceAdjustmentDTO getTailDifferenceAdjustmentDTOById(Long id);

    IPage<TailDifferenceAdjustmentVO> selectPage(TailDifferenceAdjustmentQueryDTO queryDTO);

    Boolean initData(TailDifferenceAdjustmentQueryDTO queryDTO);

    Boolean deleteByIds(List<Long> idList);

    /**
     * 批量提交
     * @param idList
     * @return
     */
    Boolean submit(List<Long> idList);

    /**
     * 批量撤回
     * @param idList
     * @return
     */
    Boolean withdraw(List<Long> idList);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean generateVoucher(List<Long> idList,String isSubmit);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    List<TailDifferenceAdjustmentDetailVO> listByConditionByIdList(List<Long> idList);

    /**
     * 批量生成凭证
     * @param idList
     * @param isSubmit
     * @return
     */
    Boolean insertVoucher(List<Long> idList,String isSubmit);

}
