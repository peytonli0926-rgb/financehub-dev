package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHisEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 偿还计划测算历史表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-10-31
 */
public interface RepaymentPlanHisMapper extends BaseMapper<RepaymentPlanHisEntity> {

    Integer selectNewVersionByContractCode(@Param("contractCode") String contractCode);

}
