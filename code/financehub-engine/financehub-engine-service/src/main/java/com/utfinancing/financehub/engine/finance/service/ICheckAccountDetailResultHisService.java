package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultHisDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultHisVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultHisEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description : CheckAccountDetailResultHis服务类接口
 * @Modified :
 */
public interface ICheckAccountDetailResultHisService extends IService<CheckAccountDetailResultHisEntity> {

    Long saveCheckAccountDetailResultHis(CheckAccountDetailResultHisDTO dto);

    Long updateCheckAccountDetailResultHis(Long id, CheckAccountDetailResultHisDTO dto);

    CheckAccountDetailResultHisDTO getCheckAccountDetailResultHisDTOById(Long id);

    IPage<CheckAccountDetailResultHisVO> selectDetailHisPage(CheckAccountDetailResultHisQueryDTO queryDTO);

    void saveResultToHis();

    void clearHisTableData(Integer periodCode);

    void queryAndSaveCheckResultDtoByParamAsync(CheckAccountDetailResultHisQueryDTO param, List<Map<String, Object>> paramList);

    void queryAndSaveCheckResultDtoAsync(Integer periodCode, String checkType, List<String> partAccountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord);

    void clearContractTmpTableData();

    void insertContractTmpTableData(Integer periodCode);

    List<CheckAccountDetailResultHisEntity> queryCheckResultDto(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord);

    void batchSaveToResultHis(CountDownLatch countDownLatch, List<CheckAccountDetailResultHisEntity> entityList, String percent, String periodCode);
}
