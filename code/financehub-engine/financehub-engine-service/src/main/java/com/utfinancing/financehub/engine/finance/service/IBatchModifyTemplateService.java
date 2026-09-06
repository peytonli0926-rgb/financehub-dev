package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchModifyTemplateVO;
import com.utfinancing.financehub.engine.finance.entity.BatchModifyTemplateEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2025-06-23
 * @Description : BatchModifyTemplate服务类接口
 * @Modified :
 */
public interface IBatchModifyTemplateService extends IService<BatchModifyTemplateEntity> {

    Long saveBatchModifyTemplate(BatchModifyTemplateDTO dto);

    Long updateBatchModifyTemplate(Long id, BatchModifyTemplateDTO dto);

    BatchModifyTemplateDTO getBatchModifyTemplateDTOById(Long id);

    IPage<BatchModifyTemplateVO> selectPage(BatchModifyTemplateQueryDTO queryDTO);

    public void clearTableData();

}
