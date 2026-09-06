package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.RentRegisterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.RentRegisterQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出租登记 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-07
 */
public interface RentRegisterMapper extends BaseMapper<RentRegisterEntity> {

    IPage<RentRegisterEntity> selectByMapper(@Param("page") Page<RentRegisterEntity> page, @Param("queryDTO") RentRegisterQueryDTO queryDTO);

    List<RentRegisterEntity> selectByMapper(@Param("queryDTO") RentRegisterQueryDTO queryDTO);
}
