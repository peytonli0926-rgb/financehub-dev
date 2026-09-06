package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordExcel;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentThirdStageRecordVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentThirdStageRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2025-12-26
 * @Description : ImpairmentThirdStageRecord服务类接口
 * @Modified :
 */
public interface IImpairmentThirdStageRecordService extends IService<ImpairmentThirdStageRecordEntity> {

    Long saveImpairmentThirdStageRecord(ImpairmentThirdStageRecordDTO dto);

    Long updateImpairmentThirdStageRecord(Long id, ImpairmentThirdStageRecordDTO dto);

    ImpairmentThirdStageRecordDTO getImpairmentThirdStageRecordDTOById(Long id);

    IPage<ImpairmentThirdStageRecordVO> selectPage(ImpairmentThirdStageRecordQueryDTO queryDTO);

    /**
     * 数据导入
     */
    public R<String> importData(List<ImpairmentThirdStageRecordExcel> list);

}
