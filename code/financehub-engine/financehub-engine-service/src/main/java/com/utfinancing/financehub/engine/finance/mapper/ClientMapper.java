package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-13
 */
public interface ClientMapper extends BaseMapper<ClientEntity> {

    String selectClientCodeByName(@Param("clientName") String clientName);
}
