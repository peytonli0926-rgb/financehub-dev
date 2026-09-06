package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.BankAccountEntity;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.BankAccountVO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : BankAccount服务类接口
 * @Modified :
 */
public interface IReportFinanceDataService{

    boolean compareOrgPeriod();

    boolean fillContractReportFlag();

    boolean syncKingdeeExchangeRate(String queryDate);

    boolean syncAssistantBalance(String queryDate);

    boolean syncDetailBalance(String queryDate);

    String executeAssistantAndDetailBalanceData(String queryDate);

    String executeTaReclassificationData(String queryDate);

    String executeKingdeeRateData(String queryDateStart, String queryDateEnd);

    String executeContractMonthData(String periodCodes, String orgIds);

    void executeContractBalanceMonthData(CountDownLatch totalCountDownLatch, String periodCodeStr, List<String> orgIdList);

}
