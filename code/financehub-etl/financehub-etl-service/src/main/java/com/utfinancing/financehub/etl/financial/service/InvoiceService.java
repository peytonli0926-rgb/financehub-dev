package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;

import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.service.InvoiceService</li>
 * <li>CreateTime : 2023/11/10 19:13</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface InvoiceService {
    /**
     * 同步电子和纸质发票数据
     * @param queryDTO
     */
    Boolean syncInvoicingSystem(InvoiceClaimQueryDTO queryDTO);

    Boolean invoiceGenerateVoucher();

    /**
     * 同步电子和纸质发票数据 开始结束时间
     * @param queryDTO
     */
    Boolean syncInvoicingSystemByDay(InvoiceClaimQueryDTO queryDTO);

}
