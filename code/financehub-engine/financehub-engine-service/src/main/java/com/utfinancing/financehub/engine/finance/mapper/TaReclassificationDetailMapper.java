package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.TaReclassificationDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(@Param("queryDate")String queryDate);

    List<TaReclassificationDetailEntity> selectTYPTInfo(@Param("queryDate") String queryDate, @Param("queryDateNextDay") String queryDateNextDay, @Param("systemCode") String systemCode);

    List<TaReclassificationDetailEntity> getTaReclassificationDetailList();

}
