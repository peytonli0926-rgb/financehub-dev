package com.utfinancing.financehub.etl.middle.service.impl;

import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeCheckMapper;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeCheckDataService;
import com.utfinancing.financehub.etl.middle.mapper.MiddleCheckMapper;
import com.utfinancing.financehub.etl.middle.service.IMiddleCheckDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@RequiredArgsConstructor
@Service
public class MiddleCheckDataServiceImpl implements IMiddleCheckDataService {

    private final MiddleCheckMapper middleCheckMapper;

//    @DS("slave_kingdee")
    @Override
    public List<Map<String, Object>> getMiddleCheckData(String periodCode) {
        String year = periodCode.substring(0,4);
        String month = periodCode.substring(4);
        return middleCheckMapper.getMiddleCheckData(year, month);
    }
}
