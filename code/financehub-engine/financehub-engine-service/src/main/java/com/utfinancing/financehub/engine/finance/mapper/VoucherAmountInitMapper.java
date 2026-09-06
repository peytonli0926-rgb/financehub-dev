package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.VoucherAmountInitEntity;

import java.util.List;

/**
 * <p>
 * 金蝶202311期合同的借方金额 Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-01-10
 */
public interface VoucherAmountInitMapper extends BaseMapper<VoucherAmountInitEntity> {
    /**
     * 根据合同汇总所有借方金额
     */
    public List<VoucherAmountInitEntity> selectDtAmountInit();
}
