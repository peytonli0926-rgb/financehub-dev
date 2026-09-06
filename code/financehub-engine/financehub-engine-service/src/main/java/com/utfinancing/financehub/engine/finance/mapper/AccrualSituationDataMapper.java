package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.AccrualSituationDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 计提情况数据 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-06-30
 */
public interface AccrualSituationDataMapper extends BaseMapper<AccrualSituationDataEntity> {

    AccrualSituationDataEntity getDataByVoucherId(@Param("voucherId") Long voucherId, @Param("fillType") String fillType);
}
