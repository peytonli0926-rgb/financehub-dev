package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailDTO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

public interface IConvertTransferThirdPartService extends IService<ConvertTransferThirdPartEntity> {
    void importFromData(List<ConvertTransferThirdPartDTO> thirdPartDTOS, List<ConvertTransferThirdPartDetailDTO> detailDTOS);

    Boolean generateVoucher(List<Long> ids);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    boolean updateProcessStatus(CommonApproveDTO dto);
}
