package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountMiddleResultDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountMiddleResultVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : CheckAccountMiddleResult服务类接口
 * @Modified :
 */
public interface ICheckAccountMiddleResultService extends IService<CheckAccountMiddleResultEntity> {

    Long saveCheckAccountMiddleResult(CheckAccountMiddleResultDTO dto);

    Long updateCheckAccountMiddleResult(Long id, CheckAccountMiddleResultDTO dto);

    CheckAccountMiddleResultDTO getCheckAccountMiddleResultDTOById(Long id);

    IPage<CheckAccountMiddleResultVO> selectMiddlePage(CheckAccountMiddleResultQueryDTO queryDTO);

    void clearTableData();

    void queryAndSaveCheckMiddleDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord);
}
