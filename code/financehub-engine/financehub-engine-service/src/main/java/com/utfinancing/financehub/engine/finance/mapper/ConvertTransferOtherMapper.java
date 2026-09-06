package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPlanVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface ConvertTransferOtherMapper extends BaseMapper<ConvertTransferOtherEntity> {

    Page<ConvertTransferOtherPlanVO> planPage(Page<Object> page, @Param("orgContractCodeMap") Map<String, Set<String>> orgContractMap,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
    Page<ConvertTransferOtherPlanVO> planWithInvoicePage(Page<Object> page, @Param("orgContractCodeMap") Map<String, Set<String>> orgContractMap, @Param("invoiceOrgContractMap") Map<String, Set<String>> invoiceOrgContractMap,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}

