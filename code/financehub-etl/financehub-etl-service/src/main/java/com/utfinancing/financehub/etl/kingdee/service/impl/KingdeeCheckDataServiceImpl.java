package com.utfinancing.financehub.etl.kingdee.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeCheckMapper;
import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeEasMapper;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeCheckDataService;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeEasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@RequiredArgsConstructor
@Service
public class KingdeeCheckDataServiceImpl implements IKingdeeCheckDataService {

    private final KingdeeCheckMapper kingdeeCheckMapper;

//    @DS("slave_kingdee")
    @Override
    public List<Map<String, Object>> getKingdeeCheckData(String periodCode) {
        return kingdeeCheckMapper.getKingdeeCheckData(periodCode);
    }
}
