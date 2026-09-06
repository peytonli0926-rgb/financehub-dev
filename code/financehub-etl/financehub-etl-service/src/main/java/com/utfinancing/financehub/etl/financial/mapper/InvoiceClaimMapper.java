package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.InvoiceClaimEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractInvoiceClaimVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-10
 */
public interface InvoiceClaimMapper extends BaseMapper<InvoiceClaimEntity> {

    IPage<ContractInvoiceClaimVO> selectContractInvoiceClaimByPage(Page page, @Param("param") InvoiceClaimQueryDTO queryDTO);

    List<ContractInvoiceClaimVO> selectContractInvoiceClaimByPage(@Param("param")InvoiceClaimQueryDTO params);

    List<ContractInvoiceClaimVO> selectContractInvoiceClaimByCondition(@Param("param") InvoiceClaimQueryDTO queryDTO);

}
