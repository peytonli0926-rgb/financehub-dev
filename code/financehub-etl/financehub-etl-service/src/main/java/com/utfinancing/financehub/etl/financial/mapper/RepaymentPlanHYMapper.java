package com.utfinancing.financehub.etl.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanXWEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RepaymentPlanHYMapper extends BaseMapper<RepaymentPlanHYEntity> {

    /**
     * 删除偿还计划
     */
    public void delRepaymentPlan(@Param("contractCodeList") List<String> contractCodeList);
}
