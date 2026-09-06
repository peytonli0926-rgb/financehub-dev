package com.utfinancing.financehub.etl.financial.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.entity.VoucherToEasRecordEntity;
import com.utfinancing.financehub.etl.financial.entity.VoucherToEasResultEntity;
import com.utfinancing.financehub.etl.financial.service.IVoucherToEasRecordService;
import com.utfinancing.financehub.etl.financial.service.IVoucherToEasResultService;
import com.utfinancing.financehub.etl.financial.service.KingdeeEASDataService;
import com.utfinancing.financehub.etl.kingdee.model.dto.EasVoucherRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class KingdeeEASDataServiceImpl implements KingdeeEASDataService {


    @Resource
    private IVoucherToEasResultService voucherToEasResultService;

    @Resource
    private IVoucherToEasRecordService voucherToEasRecordService;

    /**
     * 保存入参
     */
    public void saveParams(List<EasVoucherDTO> voucherEntryList, LocalDateTime now, String rowParams,String systemCode,String batchUuid,String metaParams) {
        // 保存传入EAS系统的参数
        List<VoucherToEasRecordEntity> voucherToEasRecordList = BeanUtil.copyToList(voucherEntryList,
                VoucherToEasRecordEntity.class);
        voucherToEasRecordList.stream().forEach(e -> {
            e.setTransactionDate(now);
//            e.setJsonParam(rowParams);
//            e.setSystemCode(systemCode);
            e.setBatchUuid(batchUuid);
            e.setMetaParams(metaParams);
        });
        voucherToEasRecordList.get(0).setJsonParam(rowParams);
        voucherToEasRecordService.saveBatch(voucherToEasRecordList);
    }


    /**
     * 保存结果
     */
    public void saveResult(List<EasVoucherRespDTO> returnRespDTO) {
        List<VoucherToEasResultEntity> voucherToEasResultList = new ArrayList<>();
        for (EasVoucherRespDTO dto : returnRespDTO) {
            VoucherToEasResultEntity entity = new VoucherToEasResultEntity();
            entity.setId(IdWorker.getId());
            entity.setReceiveTime(LocalDateTime.now());
            entity.setVoucherNumber(dto.getVoucherNumber());
            entity.setBackResult(JSONObject.toJSONString(dto));
            voucherToEasResultList.add(entity);
        }
        voucherToEasResultService.saveBatch(voucherToEasResultList);
    }

    public void createVoucherToEasResultEntity(Map param, String successFlag, LocalDateTime now,String batchUuid) {
        if (param.size() > 0) {
            List<VoucherToEasResultEntity> voucherToEasResultList = new ArrayList<>();
            for(Object key : param.keySet()) {
                VoucherToEasResultEntity entity = new VoucherToEasResultEntity();
                entity.setId(IdWorker.getId());
                if (param.get("voucherNumber") != null) {
                    entity.setVoucherNumber(param.get("voucherNumber").toString());
                }
                entity.setBackResult(key + ":" + JSONObject.toJSONString(param.get(key)));
                entity.setSuccessFlag(successFlag);
                entity.setReceiveTime(now);
                entity.setBatchUuid(batchUuid);
                voucherToEasResultList.add(entity);
            }
            voucherToEasResultService.saveBatch(voucherToEasResultList);
        }
    }
}
