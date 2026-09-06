package com.utfinancing.financehub.engine.integration.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherEntryDTO;
import com.utfinancing.financehub.engine.integration.model.vo.ExternalVoucherEntryVO;
import com.utfinancing.financehub.engine.integration.entity.ExternalVoucherEntryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description : ExternalVoucherEntry服务类接口
 * @Modified :
 */
public interface IExternalVoucherEntryService extends IService<ExternalVoucherEntryEntity> {

    Long saveExternalVoucherEntry(ExternalVoucherEntryDTO dto);

    Long updateExternalVoucherEntry(Long id, ExternalVoucherEntryDTO dto);

    ExternalVoucherEntryDTO getExternalVoucherEntryDTOById(Long id);

    IPage<ExternalVoucherEntryVO> selectPage(ExternalVoucherEntryQueryDTO queryDTO);

}
