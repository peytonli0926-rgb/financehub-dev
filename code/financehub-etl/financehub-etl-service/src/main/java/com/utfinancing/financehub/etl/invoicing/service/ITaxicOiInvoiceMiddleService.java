package com.utfinancing.financehub.etl.invoicing.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicOiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicOiInvoiceMiddleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TaxicOiInvoiceMiddle服务类接口
 * @Modified :
 */
@DS("slave_apidb")
public interface ITaxicOiInvoiceMiddleService extends IService<TaxicOiInvoiceMiddleEntity> {

    Long saveTaxicOiInvoiceMiddle(TaxicOiInvoiceMiddleDTO dto);

    Long updateTaxicOiInvoiceMiddle(Long id, TaxicOiInvoiceMiddleDTO dto);

    TaxicOiInvoiceMiddleDTO getTaxicOiInvoiceMiddleDTOById(Long id);

    IPage<TaxicOiInvoiceMiddleVO> selectPage(TaxicOiInvoiceMiddleQueryDTO queryDTO);

    List<InvoiceClaimVO> selectByCondition(TaxicOiInvoiceMiddleQueryDTO queryDTO);
}
