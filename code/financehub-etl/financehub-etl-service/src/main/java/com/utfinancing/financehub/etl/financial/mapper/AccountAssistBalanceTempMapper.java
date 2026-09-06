package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.AccountAssistBalanceTempEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 科目辅助帐余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2024-01-04
 */
public interface AccountAssistBalanceTempMapper extends BaseMapper<AccountAssistBalanceTempEntity> {

    void batchUpdateYearBeginDataByFirstMonth(@Param("periodCode") int periodCode);

    void syncIntoAccountAssist(@Param("periodCode")int periodCode);

    void batchUpdateYearBeginDataElseByFirstMonth(@Param("periodCode")int periodCode);

    void deleteTable();
}
