package com.utfinancing.financehub.etl.middle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
public interface MiddleCheckMapper {

    List<Map<String, Object>> getMiddleCheckData(@Param("year") String year, @Param("month") String month);
}
