package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempClosePeriodEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 科目辅助帐余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2024-01-04
 */
public interface AccountAssistBalanceMapper extends BaseMapper<AccountAssistBalanceEntity> {

    void removeAssistBalanceCurrentMonth(@Param("periodCode") Integer periodCode);

    List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceCurrentMonth(@Param("periodCode")Integer periodCode, @Param("prevPeriodCode")Integer prevPeriodCode);

    List<AccountAssistBalanceTempClosePeriodEntity> syncAssistBalanceNextMonth(@Param("periodCode")Integer periodCode, @Param("nextPeriodCode")Integer nextPeriodCode);

    Map<String, Long> getNextClosePeriodSeqVal();

    void setClosePeriodSeqVal(@Param("nextVal") Long nextVal);
}
