package com.utfinancing.financehub.etl.invoicingelec.service.impl;

import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicingelec.mapper.TaxicIiInvoiceElecMapper;
import com.utfinancing.financehub.etl.invoicingelec.service.ITaxicIiInvoiceElecService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
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
public class TaxicIiInvoiceElecServiceImpl implements ITaxicIiInvoiceElecService {

    private final TaxicIiInvoiceElecMapper taxicIiInvoiceElecMapper;


    @Override
    public List<InvoiceClaimVO> selectByCondition(TaxicOiInvoiceMiddleQueryDTO taxicOiInvoiceMiddleQueryDTO) {
        return taxicIiInvoiceElecMapper.selectByCondition(taxicOiInvoiceMiddleQueryDTO);
    }
}

