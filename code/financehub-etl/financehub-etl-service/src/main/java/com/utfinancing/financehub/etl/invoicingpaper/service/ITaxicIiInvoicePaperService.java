package com.utfinancing.financehub.etl.invoicingpaper.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TaxicIiInvoiceMiddle服务类接口
 * @Modified :
 */
@DS("slave_invoice_p_db")
public interface ITaxicIiInvoicePaperService {

    List<InvoiceClaimVO> selectByCondition(TaxicOiInvoiceMiddleQueryDTO taxicOiInvoiceMiddleQueryDTO);
}
