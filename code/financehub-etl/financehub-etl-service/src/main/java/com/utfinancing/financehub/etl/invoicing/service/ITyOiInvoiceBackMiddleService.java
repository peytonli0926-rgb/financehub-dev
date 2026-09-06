package com.utfinancing.financehub.etl.invoicing.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceBackMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceBackMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TyOiInvoiceBackMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TyOiInvoiceBackMiddleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : TyOiInvoiceBackMiddle服务类接口
 * @Modified :
 */
@DS("slave_apidb")
public interface ITyOiInvoiceBackMiddleService extends IService<TyOiInvoiceBackMiddleEntity> {

    Long saveTyOiInvoiceBackMiddle(TyOiInvoiceBackMiddleDTO dto);

    Long updateTyOiInvoiceBackMiddle(Long id, TyOiInvoiceBackMiddleDTO dto);

    TyOiInvoiceBackMiddleDTO getTyOiInvoiceBackMiddleDTOById(Long id);

    IPage<TyOiInvoiceBackMiddleVO> selectPage(TyOiInvoiceBackMiddleQueryDTO queryDTO);

}
