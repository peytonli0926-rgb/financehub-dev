package com.utfinancing.financehub.etl.kingdee.mapper;

import com.utfinancing.financehub.etl.kingdee.model.dto.CurrencyExchangeRateDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface KingdeeExchangeRateMapper {


    List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDate(@Param("queryDate") String queryDate);

    List<CurrencyExchangeRateDTO> selectExchangeRateByQueryDateStartAndEnd(@Param("queryDateStart") String queryDateStart, @Param("queryDateEnd") String queryDateEnd);
}
