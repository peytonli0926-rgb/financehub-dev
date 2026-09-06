package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TaOtherPayableDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : TaOtherPayableDetail服务类接口
 * @Modified :
 */
public interface ITaOtherPayableDetailService extends IService<TaOtherPayableDetailEntity> {

    Long saveTaOtherPayableDetail(TaOtherPayableDetailDTO dto);

    Long updateTaOtherPayableDetail(Long id, TaOtherPayableDetailDTO dto);

    TaOtherPayableDetailDTO getTaOtherPayableDetailDTOById(Long id);

    IPage<TaOtherPayableDetailVO> selectPage(TaOtherPayableDetailQueryDTO queryDTO);

    List<TaOtherPayableDetailVO> selectList(TaOtherPayableDetailQueryDTO taOtherPayableDetailQueryDTO);

    void removeByTaOtherPayableIdList(List<Long> ids);

    List<TaOtherPayableDetailEntity> selectDetailInfo(Long id);
}
