package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.VoucherEntryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherEntryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 凭证分录表; Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-01
 */
public interface VoucherEntryMapper extends BaseMapper<VoucherEntryEntity> {

    List<VoucherEntryVO> selectCourtCostByCondition(@Param("condition") VoucherEntryQueryDTO queryDTO);
}
