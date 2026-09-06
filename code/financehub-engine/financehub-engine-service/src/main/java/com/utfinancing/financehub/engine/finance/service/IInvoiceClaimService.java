package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.engine.finance.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.engine.finance.entity.InvoiceClaimEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-05
 * @Description : InvoiceClaim服务类接口
 * @Modified :
 */
public interface IInvoiceClaimService extends IService<InvoiceClaimEntity> {

    Long saveInvoiceClaim(InvoiceClaimDTO dto);

    Long updateInvoiceClaim(Long id, InvoiceClaimDTO dto);

    InvoiceClaimDTO getInvoiceClaimDTOById(Long id);

    IPage<InvoiceClaimVO> selectPage(InvoiceClaimQueryDTO queryDTO);

    Boolean saveBatchInvoiceClaim(List<InvoiceClaimDTO> dto);

    Long saveMqInvoiceClaim(InvoiceClaimDTO dto);

    Boolean invoiceGenerateVoucher(Boolean skipRepeatDataCheck);


    void checkInvoiceData();

    void updateInvoiceDataContractCode();
}
