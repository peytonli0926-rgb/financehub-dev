package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-03-07
 */
public interface RecyclingEquipmentInMapper extends BaseMapper<RecyclingEquipmentInEntity> {

    /**
     * @description: 通过入库日前的年月信息查询最后一次的导入数据
     * @author: zhangli.chen
     **/
    RecyclingEquipmentInEntity getLastImportedRecyclingEquipmentByYearMonth(@Param("inboundDateYearMonth") String inboundDateYearMonth);
}
