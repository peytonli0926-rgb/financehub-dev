package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.DiscountedTransferInternalEntity;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferGenerateDTO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface IDiscountedTransferInternalService extends IService<DiscountedTransferInternalEntity> {
    Boolean generatePayment(@Valid InternalTransferGenerateDTO dto);

    Boolean importFile(MultipartFile file);

    Boolean generateVoucher(List<Long> ids, YesOrNoEnum code);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);
}
