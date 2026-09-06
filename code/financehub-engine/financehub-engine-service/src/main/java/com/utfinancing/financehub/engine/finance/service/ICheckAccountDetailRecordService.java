package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailRecordDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailRecordVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description : CheckAccountDetailRecord服务类接口
 * @Modified :
 */
public interface ICheckAccountDetailRecordService extends IService<CheckAccountDetailRecordEntity> {

    Long saveCheckAccountDetailRecord(CheckAccountDetailRecordDTO dto);

    Long updateCheckAccountDetailRecord(Long id, CheckAccountDetailRecordDTO dto);

    CheckAccountDetailRecordDTO getCheckAccountDetailRecordDTOById(Long id);

    IPage<CheckAccountDetailRecordVO> selectPage(CheckAccountDetailRecordQueryDTO queryDTO);

    void checkDetail(Integer periodCode, String checkType);

    CheckAccountDetailRecordVO findRecord(Integer periodCode, String target);

    void checkKingdee(Integer periodCode, String checkType);

    void checkKingdeeMiddle(Integer periodCode, String checkType);

    void checkCommon(Integer periodCode, String businessType, String checkType);

    Boolean checkPeriodExist(CheckCommonQueryDTO queryDTO);

    void syncMiddleCheck(Integer periodCode, String checkType, CheckAccountDetailRecordEntity record, boolean currentMonth);
}
