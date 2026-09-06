package com.utfinancing.financehub.engine.verification.mapper;

import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 核销表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-10-11
 */
public interface VerificationMapper extends BaseMapper<VerificationEntity> {

    LocalDateTime getMaxAccountDate(@Param("contractCode") String contractCode, @Param("orgId") String orgId);

    BigDecimal getMaxDepreciationReserves(@Param("contractCode") String contractCode, @Param("orgId") String orgId);

    BigDecimal getMaxDepreciationReservesByPeriodCode(@Param("contractCode") String contractCode, @Param("orgId") String orgId, @Param("periodCode") Integer periodCode);
}
