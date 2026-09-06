package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.LongReceivableRegisterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LongReceivableRegisterQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 长期应收款登记 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-04-11
 */
public interface LongReceivableRegisterMapper extends BaseMapper<LongReceivableRegisterEntity> {

    IPage<LongReceivableRegisterEntity> selectByMapper(@Param("page") Page<LongReceivableRegisterEntity> page, @Param("queryDTO") LongReceivableRegisterQueryDTO queryDTO);

    List<LongReceivableRegisterEntity> selectByMapper(@Param("queryDTO") LongReceivableRegisterQueryDTO queryDTO);
}
