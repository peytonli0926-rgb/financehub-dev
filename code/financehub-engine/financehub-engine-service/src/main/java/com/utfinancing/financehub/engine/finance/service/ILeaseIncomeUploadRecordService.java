package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeUploadRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeUploadRecordVO;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeUploadRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2025-11-20
 * @Description : LeaseIncomeUploadRecord服务类接口
 * @Modified :
 */
public interface ILeaseIncomeUploadRecordService extends IService<LeaseIncomeUploadRecordEntity> {

    Long saveLeaseIncomeUploadRecord(LeaseIncomeUploadRecordDTO dto);

    Long updateLeaseIncomeUploadRecord(Long id, LeaseIncomeUploadRecordDTO dto);

    LeaseIncomeUploadRecordDTO getLeaseIncomeUploadRecordDTOById(Long id);

    IPage<LeaseIncomeUploadRecordVO> selectPage(LeaseIncomeUploadRecordQueryDTO queryDTO);

}
