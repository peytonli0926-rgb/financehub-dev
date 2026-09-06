package com.utfinancing.financehub.etl.kingdee.service.impl;

import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeExchangeRateMapper;
import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeOrgPeriodMapper;
import com.utfinancing.financehub.etl.kingdee.model.dto.CurrencyExchangeRateDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
import com.utfinancing.financehub.etl.kingdee.service.IReportKingdeeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  报表金蝶数据服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
public class ReportKingdeeDataServiceImpl implements IReportKingdeeDataService {

    @Resource
    private KingdeeOrgPeriodMapper orgPeriodMapper;

    @Resource
    private KingdeeExchangeRateMapper exchangeRateMapper;

    @Override
    public List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(List<String> orgIds) {
        return orgPeriodMapper.selectCurrentPeriodCodeByOrgIds(orgIds);
    }

    @Override
    public List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDate(String queryDate) {
        return exchangeRateMapper.selectExchangeRateByQueryDate(queryDate);
    }

    @Override
    public List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDateStartAndEnd(String queryDateStart, String queryDateEnd) {
        return exchangeRateMapper.selectExchangeRateByQueryDateStartAndEnd(queryDateStart, queryDateEnd);
    }
}

