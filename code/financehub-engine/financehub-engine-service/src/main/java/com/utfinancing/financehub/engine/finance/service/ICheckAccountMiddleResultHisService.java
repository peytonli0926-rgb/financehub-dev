package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultHisDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountMiddleResultHisVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultHisEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : CheckAccountMiddleResultHis服务类接口
 * @Modified :
 */
public interface ICheckAccountMiddleResultHisService extends IService<CheckAccountMiddleResultHisEntity> {

    Long saveCheckAccountMiddleResultHis(CheckAccountMiddleResultHisDTO dto);

    Long updateCheckAccountMiddleResultHis(Long id, CheckAccountMiddleResultHisDTO dto);

    CheckAccountMiddleResultHisDTO getCheckAccountMiddleResultHisDTOById(Long id);

    IPage<CheckAccountMiddleResultHisVO> selectMiddleHisPage(CheckAccountMiddleResultHisQueryDTO queryDTO);

    void saveResultToHis();

    void clearHisTableData(Integer periodCode);

    void queryAndSaveCheckMiddleDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord);
}
