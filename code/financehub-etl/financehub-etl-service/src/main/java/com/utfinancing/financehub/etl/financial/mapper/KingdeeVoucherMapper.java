package com.utfinancing.financehub.etl.financial.mapper;

import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryInnerDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 金蝶凭证表 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-14
 */
public interface KingdeeVoucherMapper extends BaseMapper<KingdeeVoucherEntity> {

    List<KingdeeVoucherEntryInnerDTO> selectKingdeeVoucherEntrySumByPeriod(@Param("periodCode") Integer periodCode,@Param("contractCodeList") List<String> contractCodeList);

}
