package com.utfinancing.financehub.engine.dw.mapper;

import com.utfinancing.financehub.engine.dw.entity.DwChargeOffSummaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DwChargeOffSummaryMapper extends BaseMapper<DwChargeOffSummaryEntity> {

    /**
     * 批量插入ChargeOff汇总数据
     *
     * @param entityList ChargeOff汇总数据实体列表
     * @return 插入记录数
     */
    int insertBatch(@Param("entityList") List<DwChargeOffSummaryEntity> entityList);

}