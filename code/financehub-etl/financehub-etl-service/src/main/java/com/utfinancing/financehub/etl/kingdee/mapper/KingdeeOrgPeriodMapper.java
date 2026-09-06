package com.utfinancing.financehub.etl.kingdee.mapper;

import com.utfinancing.financehub.etl.kingdee.model.dto.OrgPeriodDTO;
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
public interface KingdeeOrgPeriodMapper {


    List<OrgPeriodDTO> selectCurrentPeriodCodeByOrgIds(@Param("param") List<String> orgIds);

}
