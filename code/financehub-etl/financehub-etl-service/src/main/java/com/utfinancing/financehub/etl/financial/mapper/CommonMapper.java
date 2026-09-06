package com.utfinancing.financehub.etl.financial.mapper;

import java.util.List;
import java.util.Map;

public interface CommonMapper {
    List<Map<String, Object>> selectDataCommon(String querySql);

}
