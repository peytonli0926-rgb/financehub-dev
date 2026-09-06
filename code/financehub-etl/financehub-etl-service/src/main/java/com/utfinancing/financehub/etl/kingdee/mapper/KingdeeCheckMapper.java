package com.utfinancing.financehub.etl.kingdee.mapper;

import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
public interface KingdeeCheckMapper {

    List<Map<String, Object>> getKingdeeCheckData(String periodCode);
}
