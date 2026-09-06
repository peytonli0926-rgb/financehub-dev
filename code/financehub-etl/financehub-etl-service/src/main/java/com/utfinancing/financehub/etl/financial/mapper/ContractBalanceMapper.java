package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.financial.entity.ContractBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同余额表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
public interface ContractBalanceMapper extends BaseMapper<ContractBalanceEntity> {

    boolean insertContractBalance(@Param("columns") List<String> columns, @Param("values")List<Object> values);

}
