package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.kingdee.model.dto.CurrencyExchangeRateDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@DS("slave_kingdee")
public interface IReportKingdeeDataService {
    List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(List<String> orgIds);

    List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDate(String queryDate);

    List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDateStartAndEnd(String queryDateStart, String queryDateEnd);
}
