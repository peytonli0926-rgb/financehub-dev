package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPaymentDTO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

public interface IConvertTransferOtherPaymentService extends IService<ConvertTransferOtherPaymentEntity> {
    void importPlan(List<ConvertTransferOtherPaymentDTO> dtos);

    void importPayment(List<ConvertTransferOtherPaymentDTO> dtos);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    Boolean generateVoucher(List<Long> ids);
}
