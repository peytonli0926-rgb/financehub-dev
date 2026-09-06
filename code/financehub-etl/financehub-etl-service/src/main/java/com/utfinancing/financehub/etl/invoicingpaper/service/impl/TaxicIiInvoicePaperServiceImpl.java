package com.utfinancing.financehub.etl.invoicingpaper.service.impl;

import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicingpaper.mapper.TaxicIiInvoicePaperMapper;
import com.utfinancing.financehub.etl.invoicingpaper.service.ITaxicIiInvoicePaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TaxicIiInvoiceMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaxicIiInvoicePaperServiceImpl implements ITaxicIiInvoicePaperService {

    private final TaxicIiInvoicePaperMapper taxicIiInvoicePaperMapper;


    @Override
    public List<InvoiceClaimVO> selectByCondition(TaxicOiInvoiceMiddleQueryDTO taxicOiInvoiceMiddleQueryDTO) {
        return taxicIiInvoicePaperMapper.selectByCondition(taxicOiInvoiceMiddleQueryDTO);
    }
}

