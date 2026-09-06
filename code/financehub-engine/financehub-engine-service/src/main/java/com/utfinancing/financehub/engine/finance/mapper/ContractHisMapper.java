package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ContractHisEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractHisQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同历史表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-11-01
 */
public interface ContractHisMapper extends BaseMapper<ContractHisEntity> {

    List<ContractHisEntity> selectContractHisInfo(Page page, @Param("param") ContractHisQueryDTO param);

    List<ContractHisEntity> selectContractHisInfo(@Param("param") ContractHisQueryDTO param);
}
