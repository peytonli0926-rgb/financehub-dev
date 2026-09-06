package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailContractBalanceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-05-08
 */
public interface CheckAccountDetailContractBalanceMapper extends BaseMapper<CheckAccountDetailContractBalanceEntity> {

    List<CheckAccountDetailContractBalanceEntity> selectContractTmpTableData(@Param("periodCode") Integer periodCode);
}
