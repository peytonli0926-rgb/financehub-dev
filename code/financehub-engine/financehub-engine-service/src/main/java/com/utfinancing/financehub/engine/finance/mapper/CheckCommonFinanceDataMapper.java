package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 中台对账数据原始表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
public interface CheckCommonFinanceDataMapper extends BaseMapper<CheckCommonFinanceDataEntity> {
    List<Map<String, Object>> selectDataCommon(String querySql);

    void deleteCheckCommonData(@Param("businessType") String businessType);

    void deleteCheckCommonDataByExecuteDate(@Param("executeDateCode") Integer executeDateCode, @Param("businessType") String businessType, @Param("periodCode") Integer periodCode);
}
