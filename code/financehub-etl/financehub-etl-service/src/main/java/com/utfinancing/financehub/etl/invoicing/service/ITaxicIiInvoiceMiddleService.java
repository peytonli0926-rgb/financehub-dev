package com.utfinancing.financehub.etl.invoicing.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicIiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicIiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicIiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicIiInvoiceMiddleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TaxicIiInvoiceMiddle服务类接口
 * @Modified :
 */
@DS("slave_apidb")
public interface ITaxicIiInvoiceMiddleService extends IService<TaxicIiInvoiceMiddleEntity> {

    Long saveTaxicIiInvoiceMiddle(TaxicIiInvoiceMiddleDTO dto);

    Long updateTaxicIiInvoiceMiddle(Long id, TaxicIiInvoiceMiddleDTO dto);

    TaxicIiInvoiceMiddleDTO getTaxicIiInvoiceMiddleDTOById(Long id);

    IPage<TaxicIiInvoiceMiddleVO> selectPage(TaxicIiInvoiceMiddleQueryDTO queryDTO);

}
