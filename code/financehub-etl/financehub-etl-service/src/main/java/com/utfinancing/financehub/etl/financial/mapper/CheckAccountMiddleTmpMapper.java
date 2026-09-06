package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountMiddleTmpEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-28
 */
public interface CheckAccountMiddleTmpMapper extends BaseMapper<CheckAccountMiddleTmpEntity> {

    void clearTableData();
}
