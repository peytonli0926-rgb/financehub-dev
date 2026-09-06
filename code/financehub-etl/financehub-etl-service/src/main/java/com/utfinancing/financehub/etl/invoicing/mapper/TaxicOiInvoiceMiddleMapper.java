package com.utfinancing.financehub.etl.invoicing.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicOiInvoiceMiddleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-09
 */
@DS("slave_apidb")
public interface TaxicOiInvoiceMiddleMapper extends BaseMapper<TaxicOiInvoiceMiddleEntity> {

    List<InvoiceClaimVO> selectByCondition(@Param("queryDTO") TaxicOiInvoiceMiddleQueryDTO queryDTO);
}
