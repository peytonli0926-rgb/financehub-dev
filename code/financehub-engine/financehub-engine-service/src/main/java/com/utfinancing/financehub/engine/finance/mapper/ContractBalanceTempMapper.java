package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ContractBalanceTempEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-02-05
 */
public interface ContractBalanceTempMapper extends BaseMapper<ContractBalanceTempEntity> {

    boolean insertContractBalanceTemp(@Param("columns") List<String> columns, @Param("values")List<Object> values);
}
