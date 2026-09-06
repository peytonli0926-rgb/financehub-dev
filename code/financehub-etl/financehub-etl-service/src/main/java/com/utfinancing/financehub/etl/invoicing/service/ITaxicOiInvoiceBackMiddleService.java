package com.utfinancing.financehub.etl.invoicing.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceBackMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceBackMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicOiInvoiceBackMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicOiInvoiceBackMiddleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TaxicOiInvoiceBackMiddle服务类接口
 * @Modified :
 */
@DS("slave_apidb")
public interface ITaxicOiInvoiceBackMiddleService extends IService<TaxicOiInvoiceBackMiddleEntity> {

    Long saveTaxicOiInvoiceBackMiddle(TaxicOiInvoiceBackMiddleDTO dto);

    Long updateTaxicOiInvoiceBackMiddle(Long id, TaxicOiInvoiceBackMiddleDTO dto);

    TaxicOiInvoiceBackMiddleDTO getTaxicOiInvoiceBackMiddleDTOById(Long id);

    IPage<TaxicOiInvoiceBackMiddleVO> selectPage(TaxicOiInvoiceBackMiddleQueryDTO queryDTO);

}
