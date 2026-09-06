package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
public interface VoucherEntryMapper extends BaseMapper<VoucherEntryEntity> {

    int removeByIds(@Param("ids") List<String> setInvalidIds);
}
