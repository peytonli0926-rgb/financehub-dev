package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TaReclassificationDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TaReclassificationDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : TaReclassificationDetail服务类接口
 * @Modified :
 */
public interface ITaReclassificationDetailService extends IService<TaReclassificationDetailEntity> {

    Long saveTaReclassificationDetail(TaReclassificationDetailDTO dto);

    Long updateTaReclassificationDetail(Long id, TaReclassificationDetailDTO dto);

    TaReclassificationDetailDTO getTaReclassificationDetailDTOById(Long id);

    IPage<TaReclassificationDetailVO> selectPage(TaReclassificationDetailQueryDTO queryDTO);

    List<TaReclassificationDetailVO> selectList(TaReclassificationDetailQueryDTO taReclassificationDetailQueryDTO);

    List<TaReclassificationDetailEntity> getByParams(Map<String, String> param);

    void removeByTaReclassificationIdList(List<Long> ids);

    List<TaReclassificationDetailEntity> getTaReclassificationDetailList();

    List<TaReclassificationDetailEntity> selectTYPTInfo(String queryDate, String reclassificationMonthNextDay, String code);

    List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(String queryDate);
}
