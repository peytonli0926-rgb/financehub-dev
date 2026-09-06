package com.utfinancing.financehub.etl.kingdee.mapper;

import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdVouchertypesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface TBdVouchertypesMapper extends BaseMapper<TBdVouchertypesEntity> {

    List<VoucherTypeDTO> selectAllVoucherTypes();

}
