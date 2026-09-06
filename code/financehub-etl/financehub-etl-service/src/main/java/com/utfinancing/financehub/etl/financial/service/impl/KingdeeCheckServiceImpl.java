package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountKingdeeTmpEntity;
import com.utfinancing.financehub.etl.financial.service.IAccountService;
import com.utfinancing.financehub.etl.financial.service.ICheckAccountKingdeeTmpService;
import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeCheckMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeCheckService;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeCheckDataService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@RequiredArgsConstructor
@Service
//@Transactional
public class KingdeeCheckServiceImpl implements IKingdeeCheckService {

//    @Resource
    private final ICheckAccountKingdeeTmpService checkAccountKingdeeTmpService;

    private final IKingdeeCheckDataService kingdeeCheckDataService;

    @Override
    public String saveKingdeeDataToTmp(String periodCode) {
        List<Map<String, Object>> list = kingdeeCheckDataService.getKingdeeCheckData(periodCode);
        if(CollectionUtil.isEmpty(list)){
//            throw new ServiceException("未找到periodCode"+periodCode+" 内的对账数据");
            return "未找到periodCode"+periodCode+" 内的对账数据";
        }

        checkAccountKingdeeTmpService.clearTableData();
        Integer period = Integer.valueOf(periodCode);
        List<CheckAccountKingdeeTmpEntity> tmpEntityList = Lists.newArrayList();
        for(int i=0; i<list.size();i++){
            Map<String, Object> map = list.get(i);
            CheckAccountKingdeeTmpEntity tmpEntity = new CheckAccountKingdeeTmpEntity();
            tmpEntity.setPeriodCode(period);
            tmpEntity.setCurrencyType(getString(map.get("CURRENCY_TYPE")));
            tmpEntity.setAccountCode(getString(map.get("ACCOUNT_CODE")));
            tmpEntity.setOrgId(getString(map.get("ORG_ID")));
            tmpEntity.setFbeginBalanceFor(getBigDecimal(map.get("FBEGINBALANCEFOR")));
            tmpEntity.setFendBalanceFor(getBigDecimal(map.get("FENDBALANCEFOR")));
            tmpEntityList.add(tmpEntity);
        }
        checkAccountKingdeeTmpService.saveBatch(tmpEntityList);
        return "success";
    }

    private static String getString(Object column) {
        return ObjectUtil.isEmpty(column) ? null : column.toString();
    }

    private static BigDecimal getBigDecimal(Object column) {
        return ObjectUtil.isEmpty(column) ? BigDecimal.ZERO : (BigDecimal) column;
    }
}
