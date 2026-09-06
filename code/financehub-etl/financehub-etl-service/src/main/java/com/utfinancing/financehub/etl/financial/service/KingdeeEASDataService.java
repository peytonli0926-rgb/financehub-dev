package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.EasVoucherRespDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface KingdeeEASDataService {

    /**
     * 保存入参
     */
    public void saveParams(List<EasVoucherDTO> voucherEntryList, LocalDateTime now, String rowParams,String systemCode,String batchUuid,String metaParams);


    /**
     * 保存结果
     */
    public void saveResult(List<EasVoucherRespDTO> returnRespDTO);

    public void createVoucherToEasResultEntity(Map param, String successFlag, LocalDateTime now,String batchUuid);
}
