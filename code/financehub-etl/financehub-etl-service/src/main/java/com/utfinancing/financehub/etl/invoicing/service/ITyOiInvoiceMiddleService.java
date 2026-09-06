package com.utfinancing.financehub.etl.invoicing.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicOiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TyOiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TyOiInvoiceMiddleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TyOiInvoiceMiddle服务类接口
 * @Modified :
 */
@DS("slave_apidb")
public interface ITyOiInvoiceMiddleService extends IService<TyOiInvoiceMiddleEntity> {

    Long saveTyOiInvoiceMiddle(TyOiInvoiceMiddleDTO dto);

    Long updateTyOiInvoiceMiddle(Long id, TyOiInvoiceMiddleDTO dto);

    TyOiInvoiceMiddleDTO getTyOiInvoiceMiddleDTOById(Long id);

    IPage<TyOiInvoiceMiddleVO> selectPage(TyOiInvoiceMiddleQueryDTO queryDTO);

    List<TyOiInvoiceMiddleVO> selectByCondition(TyOiInvoiceMiddleQueryDTO queryDTO);

}
