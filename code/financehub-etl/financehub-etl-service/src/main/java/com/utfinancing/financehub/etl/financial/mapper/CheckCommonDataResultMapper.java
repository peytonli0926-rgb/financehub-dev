package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 业务系统对账数据结果表 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-04-01
 */
public interface CheckCommonDataResultMapper extends BaseMapper<CheckCommonDataResultEntity> {

    void clearTableData(@Param("executeDateCode") String executeDateCode, @Param("sqlMark") String sqlMark, @Param("periodCode") Integer periodCode);
}
