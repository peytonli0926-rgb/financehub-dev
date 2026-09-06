package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultHisDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountKingdeeResultHisVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountKingdeeResultHisEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : CheckAccountKingdeeResultHis服务类接口
 * @Modified :
 */
public interface ICheckAccountKingdeeResultHisService extends IService<CheckAccountKingdeeResultHisEntity> {

    Long saveCheckAccountKingdeeResultHis(CheckAccountKingdeeResultHisDTO dto);

    Long updateCheckAccountKingdeeResultHis(Long id, CheckAccountKingdeeResultHisDTO dto);

    CheckAccountKingdeeResultHisDTO getCheckAccountKingdeeResultHisDTOById(Long id);

    IPage<CheckAccountKingdeeResultHisVO> selectKingdeeHisPage(CheckAccountKingdeeResultHisQueryDTO queryDTO);

    void queryAndSaveCheckKingdeeDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord);

    void saveResultToHis();

    void clearHisTableData(Integer periodCode);
}
