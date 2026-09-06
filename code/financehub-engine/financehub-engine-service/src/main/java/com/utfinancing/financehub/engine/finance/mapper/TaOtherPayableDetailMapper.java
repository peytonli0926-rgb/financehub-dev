package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.TaOtherPayableDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDetailQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * ta其他应付款明细 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-05-22
 */
public interface TaOtherPayableDetailMapper extends BaseMapper<TaOtherPayableDetailEntity> {

    IPage<TaOtherPayableDetailEntity> selectByMapper(@Param("page") Page<TaOtherPayableDetailEntity> page, @Param("queryDTO") TaOtherPayableDetailQueryDTO queryDTO);

    List<TaOtherPayableDetailEntity> selectByMapper(@Param("queryDTO") TaOtherPayableDetailQueryDTO queryDTO);

    List<TaOtherPayableDetailEntity> selectDetailInfo(@Param("taId") Long id);
}
