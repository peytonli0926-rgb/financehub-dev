package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.OrgFundClaimJobRecordEntity;

import java.util.Map;

public interface IOrgFundClaimJobRecordService extends IService<OrgFundClaimJobRecordEntity> {

    /**
     * @description: 获取最后一次的执行记录
     **/
    public OrgFundClaimJobRecordEntity getLastJobRecord();

    /**
     * @description: 保存调度记录
     **/
    public void saveFundClaimJobRecord(Map<String, Object> params,String paramStr);

}
