package com.utfinancing.financehub.etl.middle.service;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@DS("slave_middle")
public interface IMiddleCheckDataService {
    List<Map<String, Object>> getMiddleCheckData(String periodCode);

}
