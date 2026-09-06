package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempFirstMonthEntity;

/**
 * <p>
 * 科目辅助帐余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2024-01-04
 */
public interface AccountAssistBalanceTempFirstMonthMapper extends BaseMapper<AccountAssistBalanceTempFirstMonthEntity> {

    void deleteTable();
}
