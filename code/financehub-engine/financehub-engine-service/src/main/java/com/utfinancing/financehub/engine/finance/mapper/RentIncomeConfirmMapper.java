package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RentIncomeConfirmEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 租金收入确认 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-10
 */
public interface RentIncomeConfirmMapper extends BaseMapper<RentIncomeConfirmEntity> {

    /**
     * 查询上月结转金额
     * @param contractCode
     * @param clientCode
     * @param orgId
     * @return
     */
    BigDecimal selectPreviousCarryOverAmount(@Param("contractCode") String contractCode, @Param("clientCode") String clientCode, @Param("orgId") String orgId);

    /**
     * 查询本月结转金额
     * @param contractCode
     * @param clientCode
     * @param orgId
     * @return
     */
    BigDecimal selectCarryOverAmount(@Param("contractCode") String contractCode, @Param("clientCode") String clientCode, @Param("orgId") String orgId);
}
