package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.AccountPeriodEntity;
import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会计期间 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
public interface FinanceOrgPeriodMapper {

    List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(@Param("param") List<String> orgIds);

}
