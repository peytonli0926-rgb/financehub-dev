package com.utfinancing.financehub.etl.financial.service;

import java.util.List;
import java.util.Map;

public interface ICommonService {
    List<Map<String, Object>> selectDataCommon(String querySql);
}
