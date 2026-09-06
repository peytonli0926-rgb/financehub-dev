package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 科目 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
public interface ReportFinanceDataMapper {

    void syncPeriodContractBalanceData(Integer periodCode);

    void removeContractReportFlag();

    void fillContractReportFlagOne(Integer periodCode);

    void fillContractReportFlagRest();

    void syncAssistantBalance(String queryDate);

    void syncDetailBalance(String queryDate);

    void syncContractBalanceMonthData(@Param("param") Map<String, Object> param);

    void deleteIdTemp();

    void fillIdTemp(@Param("param") Map<String, Object> param);
}
