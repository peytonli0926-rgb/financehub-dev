package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartDetailVO;

import javax.validation.constraints.NotNull;

public interface IConvertTransferThirdPartDetailService extends IService<ConvertTransferThirdPartDetailEntity> {
    IPage<ConvertTransferThirdPartCheckVO> check(ConvertTransferThirdPartDetailQueryDTO dto);
}
