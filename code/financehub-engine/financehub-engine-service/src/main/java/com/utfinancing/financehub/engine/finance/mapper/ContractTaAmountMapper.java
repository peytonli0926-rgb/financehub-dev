package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ContractTaAmountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectTaAmountInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectTaAmountOutputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-01-26
 */
public interface ContractTaAmountMapper extends BaseMapper<ContractTaAmountEntity> {

    /**
     * 根据合同列表取得合同对应的ta金额
     */
    public List<SelectTaAmountOutputDTO> selectTaAmount(@Param("params") SelectTaAmountInputDTO params);
}
