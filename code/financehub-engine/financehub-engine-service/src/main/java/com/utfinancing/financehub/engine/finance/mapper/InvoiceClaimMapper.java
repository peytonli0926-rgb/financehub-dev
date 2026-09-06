package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.InvoiceClaimEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import org.apache.ibatis.annotations.Param;

import com.utfinancing.financehub.etl.model.vo.ContractInvoiceClaimVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-01-05
 */
public interface InvoiceClaimMapper extends BaseMapper<InvoiceClaimEntity> {

    void checkInvoiceData();

    void updateInvoiceDataContractCode();

    List<ContractInvoiceClaimVO> queryContractInvoiceClaimForSyncData(@Param("param") InvoiceClaimQueryDTO params);

    List<RawTransactionDataDuplicateVo> getDuplicateData();

}
