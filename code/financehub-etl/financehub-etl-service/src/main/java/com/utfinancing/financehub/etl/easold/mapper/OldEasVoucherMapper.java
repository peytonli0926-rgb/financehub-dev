package com.utfinancing.financehub.etl.easold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.easold.entity.TGlVoucherEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface OldEasVoucherMapper extends BaseMapper<TGlVoucherEntity> {

    List<EasVoucherDTO> queryEas1Voucher(@Param("params") QueryEas1VoucherInputDTO params);
}
