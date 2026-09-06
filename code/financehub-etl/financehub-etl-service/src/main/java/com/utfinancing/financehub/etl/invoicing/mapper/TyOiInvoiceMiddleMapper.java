package com.utfinancing.financehub.etl.invoicing.mapper;

import com.utfinancing.financehub.etl.invoicing.entity.TyOiInvoiceMiddleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TyOiInvoiceMiddleVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-09
 */
public interface TyOiInvoiceMiddleMapper extends BaseMapper<TyOiInvoiceMiddleEntity> {

    List<TyOiInvoiceMiddleVO> selectByCondition(TyOiInvoiceMiddleQueryDTO queryDTO);
}
