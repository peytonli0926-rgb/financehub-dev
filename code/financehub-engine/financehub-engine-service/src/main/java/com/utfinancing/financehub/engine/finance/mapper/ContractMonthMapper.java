package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.ContractMonthEntity;

import java.util.List;

/**
 * <p>
 * 合同月表 Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2025-04-21
 */
public interface ContractMonthMapper extends BaseMapper<ContractMonthEntity> {
    /**
     * 合同月表数据同步
     */
    public void dataSync();

    /**
     * 同步合同状态、到期日、起租日
     */
    public void updateColumn();

    void updateBatchByContractCode(ContractMonthEntity updateContractMonthEntities);
}
