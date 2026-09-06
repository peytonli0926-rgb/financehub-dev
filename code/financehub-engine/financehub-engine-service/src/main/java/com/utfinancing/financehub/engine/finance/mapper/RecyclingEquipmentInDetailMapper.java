package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-07
 */
public interface RecyclingEquipmentInDetailMapper extends BaseMapper<RecyclingEquipmentInDetailEntity> {

    List<RecyclingEquipmentInCheckVO> getCheckData(@Param("inboundDate") String inboundDate, @Param("periodCode") String periodCode, @Param("orgId") String orgId, @Param("recycleId") Long recycleId);

    RecyclingEquipmentInDetailVO getProvision(@Param("contractCode") String contractCode, @Param("orgId") String orgId, @Param("processStatusList") List<String> processStatusList);
}
