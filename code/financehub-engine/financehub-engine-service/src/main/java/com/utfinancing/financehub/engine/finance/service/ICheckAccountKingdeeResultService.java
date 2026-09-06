package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountKingdeeResultVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountKingdeeResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : CheckAccountKingdeeResult服务类接口
 * @Modified :
 */
public interface ICheckAccountKingdeeResultService extends IService<CheckAccountKingdeeResultEntity> {

    Long saveCheckAccountKingdeeResult(CheckAccountKingdeeResultDTO dto);

    Long updateCheckAccountKingdeeResult(Long id, CheckAccountKingdeeResultDTO dto);

    CheckAccountKingdeeResultDTO getCheckAccountKingdeeResultDTOById(Long id);

    IPage<CheckAccountKingdeeResultVO> selectKingdeePage(CheckAccountKingdeeResultQueryDTO queryDTO);

    void queryAndSaveCheckKingdeeDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord);

    void clearTableData();
}
