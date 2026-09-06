package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@DS("slave_kingdee")
public interface IKingdeeCheckDataService {
    List<Map<String, Object>> getKingdeeCheckData(String periodCode);

}
