package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.OutstandingAmountInitEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectEndBalForInputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2025-05-15
 */
public interface OutstandingAmountInitMapper extends BaseMapper<OutstandingAmountInitEntity> {

    /**
     * 查询合同的未首先收益总额
     */
    public List<OutstandingAmountInitEntity> selectEndBalFor (@Param(value = "params")SelectEndBalForInputDTO params);
}
