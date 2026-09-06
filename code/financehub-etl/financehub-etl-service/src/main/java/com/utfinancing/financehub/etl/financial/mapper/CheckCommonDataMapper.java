package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-30
 */
public interface CheckCommonDataMapper extends BaseMapper<CheckCommonDataEntity> {

    void clearTableData(@Param("executeDateCode") String executeDateCode, @Param("sqlMark") String sqlMark, @Param("periodCode") Integer periodCode);
}
