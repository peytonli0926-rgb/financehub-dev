package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IConvertTransferThirdPartPaymentService extends IService<ConvertTransferThirdPartPaymentEntity> {
    void generate(ConvertTransferThirdPartPaymentGenerateDTO dto);

    void importPartPayment(List<ConvertTransferThirdPartPaymentDTO> dtos);

    void generateVoucher(List<Long> paymentIds);

    void submit(List<Long> paymentIds);

    void withdraw(List<Long> paymentIds);

    void updateProcessStatus(CommonApproveDTO dto);

    IPage<ConvertTransferThirdPartPaymentDetailVO> detailPage(@NotNull(message = "请选择一个支付信息") Long paymentId, int pageNum, int pageSize);

    List<ConvertTransferThirdPartPaymentDetailVO> detailExport(@NotNull(message = "请选择一个支付信息") Long paymentId);
}
