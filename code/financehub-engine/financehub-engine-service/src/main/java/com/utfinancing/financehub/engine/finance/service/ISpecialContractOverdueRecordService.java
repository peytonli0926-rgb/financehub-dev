package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordExcel;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.SpecialContractOverdueRecordVO;
import com.utfinancing.financehub.engine.finance.entity.SpecialContractOverdueRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2025-11-21
 * @Description : SpecialContractOverdueRecord服务类接口
 * @Modified :
 */
public interface ISpecialContractOverdueRecordService extends IService<SpecialContractOverdueRecordEntity> {

    Long saveSpecialContractOverdueRecord(SpecialContractOverdueRecordDTO dto);

    Long updateSpecialContractOverdueRecord(Long id, SpecialContractOverdueRecordDTO dto);

    SpecialContractOverdueRecordDTO getSpecialContractOverdueRecordDTOById(Long id);

    IPage<SpecialContractOverdueRecordVO> selectPage(SpecialContractOverdueRecordQueryDTO queryDTO);

    public R<String> importData(List<SpecialContractOverdueRecordExcel> list);
}
