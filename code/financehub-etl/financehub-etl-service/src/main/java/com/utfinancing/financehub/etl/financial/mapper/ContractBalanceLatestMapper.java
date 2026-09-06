package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.financial.entity.ContractBalanceLatestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 最新的余额表数据 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-12-02
 */
public interface ContractBalanceLatestMapper extends BaseMapper<ContractBalanceLatestEntity> {

    boolean insertContractBalance(@Param("columns") List<String> columns, @Param("values")List<Object> values);

    boolean updateContractBalance(@Param("paramMap")Map<String, Object> paramMap, @Param("id")Long id);
}
