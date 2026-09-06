package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 租赁收益 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-10
 */
public interface LeaseIncomeMapper extends BaseMapper<LeaseIncomeEntity> {

    List<LeaseIncomeEntity> countFromDetailData(@Param("param") LeaseIncomeQueryDTO queryDTO);
}
