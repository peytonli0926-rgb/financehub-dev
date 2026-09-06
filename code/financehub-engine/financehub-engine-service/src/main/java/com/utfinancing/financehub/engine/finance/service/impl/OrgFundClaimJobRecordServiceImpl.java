package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgClaimEbankNoEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgFundClaimJobRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.OrgClaimEbankNoMapper;
import com.utfinancing.financehub.engine.finance.mapper.OrgFundClaimJobRecordMapper;
import com.utfinancing.financehub.engine.finance.service.IOrgClaimEbankNoService;
import com.utfinancing.financehub.engine.finance.service.IOrgFundClaimJobRecordService;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/7/22 17:19
 */

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OrgFundClaimJobRecordServiceImpl extends ServiceImpl<OrgFundClaimJobRecordMapper, OrgFundClaimJobRecordEntity> implements IOrgFundClaimJobRecordService {

    /**
     * @description: 获取最后一次的执行记录
     **/
    @Override
    public OrgFundClaimJobRecordEntity getLastJobRecord() {
        LambdaQueryWrapper<OrgFundClaimJobRecordEntity> lambdaQueryWrapper = Wrappers.<OrgFundClaimJobRecordEntity>lambdaQuery();
        lambdaQueryWrapper.eq(OrgFundClaimJobRecordEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        lambdaQueryWrapper.orderByDesc(OrgFundClaimJobRecordEntity::getLastQueryDate);
        lambdaQueryWrapper.orderByDesc(OrgFundClaimJobRecordEntity::getCreateTime);
        lambdaQueryWrapper.last("limit 1");
        OrgFundClaimJobRecordEntity entity = this.getOne(lambdaQueryWrapper);
        return entity;
    }

    /**
     * @description: 保存调度记录
     */
    @Override
    public void saveFundClaimJobRecord(Map<String, Object> params,String paramStr) {
        OrgFundClaimJobRecordEntity jobRecordEntity = new OrgFundClaimJobRecordEntity();
        if(params!=null){
            if(!StringUtils.isEmpty(params.get(FinanceEngineEnum.FundSystemQueryParameters.START_DATE.getKey()))){
                jobRecordEntity.setStartDate(String.valueOf(params.get(FinanceEngineEnum.FundSystemQueryParameters.START_DATE.getKey())));
            }
            if(!StringUtils.isEmpty(params.get(FinanceEngineEnum.FundSystemQueryParameters.END_DATE.getKey()))){
                jobRecordEntity.setEndDate(String.valueOf(params.get(FinanceEngineEnum.FundSystemQueryParameters.END_DATE.getKey())));
            }
            jobRecordEntity.setLastQueryDate(paramStr);
            this.saveOrUpdate(jobRecordEntity);
        }
    }
}

