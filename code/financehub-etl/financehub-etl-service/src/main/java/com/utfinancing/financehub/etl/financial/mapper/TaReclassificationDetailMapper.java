package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ta重分类明细表 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-05-22
 */
public interface TaReclassificationDetailMapper extends BaseMapper<TaReclassificationDetailEntity> {

    List<TaReclassificationDetailEntity> getByParams(@Param("param") Map<String, String> param);

    List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(@Param("queryDate") String queryDate);

    List<TaReclassificationDetailEntity> selectTYPTInfo(@Param("queryDate") String queryDate, @Param("queryDateNextDay") String queryDateNextDay, @Param("systemCode") String systemCode);

}
