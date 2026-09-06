package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description : CheckAccountDetailResult服务类接口
 * @Modified :
 */
public interface ICheckAccountDetailResultService extends IService<CheckAccountDetailResultEntity> {

    Long saveCheckAccountDetailResult(CheckAccountDetailResultDTO dto);

    Long updateCheckAccountDetailResult(Long id, CheckAccountDetailResultDTO dto);

    CheckAccountDetailResultDTO getCheckAccountDetailResultDTOById(Long id);

    IPage<CheckAccountDetailResultVO> selectDetailPage(CheckAccountDetailResultQueryDTO queryDTO);

    void clearTableData();

    void queryAndSaveCheckResultDtoByParamAsync(CheckAccountDetailResultQueryDTO param, List<Map<String, Object>> paramList);

    void queryAndSaveCheckResultDtoAsync(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord);

    void batchSaveToResult(CountDownLatch countDownLatch, List<CheckAccountDetailResultEntity> entityList, String percent, String periodCode);

    List<CheckAccountDetailResultEntity> queryCheckResultDto(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord);
}
