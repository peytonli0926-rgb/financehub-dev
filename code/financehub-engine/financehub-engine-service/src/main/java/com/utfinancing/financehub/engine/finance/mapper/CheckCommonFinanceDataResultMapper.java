package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中台对账数据结果表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
public interface CheckCommonFinanceDataResultMapper extends BaseMapper<CheckCommonFinanceDataResultEntity> {

    void deleteCheckCommonData(@Param("businessType") String businessType);

    void deleteCheckCommonDataByExecuteDate(@Param("executeDateCode") Integer executeDateCode, @Param("businessType") String businessType, @Param("periodCode") Integer periodCode);
}
