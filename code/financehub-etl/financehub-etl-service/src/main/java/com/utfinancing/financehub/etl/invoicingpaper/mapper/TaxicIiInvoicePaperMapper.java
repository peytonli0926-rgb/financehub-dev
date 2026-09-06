package com.utfinancing.financehub.etl.invoicingpaper.mapper;

import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
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
public interface TaxicIiInvoicePaperMapper {
    List<InvoiceClaimVO> selectByCondition(@Param("queryDTO") TaxicOiInvoiceMiddleQueryDTO queryDTO);
}
