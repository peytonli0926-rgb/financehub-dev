package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ServiceNoAmortizationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectAssessedAmountInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectAssessedAmountOutputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-06-05
 */
public interface ServiceNoAmortizationMapper extends BaseMapper<ServiceNoAmortizationEntity> {
    public List<SelectAssessedAmountOutputDTO> selectAssessedAmount(@Param("params") SelectAssessedAmountInputDTO params);
}
