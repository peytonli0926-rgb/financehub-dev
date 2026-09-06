package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.FundSystemBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资金系统余额表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-12-05
 */
public interface FundSystemBalanceMapper extends BaseMapper<FundSystemBalanceEntity> {
    boolean insertFundSystemBalance(@Param("columns") List<String> columns, @Param("values")List<Object> values);

}
